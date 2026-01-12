package kr.co.goldenhome.implement;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import kr.co.goldenhome.dto.FacilityRecommendResponse;
import kr.co.goldenhome.dto.OpenAiEmbeddingRequest;
import kr.co.goldenhome.dto.OpenAiEmbeddingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.*;

@Slf4j
//@Component
@RequiredArgsConstructor
public class EmbeddingClient {

    private final ElasticsearchClient elasticsearchClient;
    @Value("${spring.ai.openai.api-key}")
    private String openApiKey;

    public List<List<Float>> getBatchEmbeddings(List<String> texts) {
        if (texts == null || texts.isEmpty() || texts.get(0).trim().isEmpty()) {
            throw new IllegalArgumentException("검색어를 입력해주세요.");
        }

        OpenAiEmbeddingRequest request = new OpenAiEmbeddingRequest("text-embedding-3-small", texts);
        RestClient restClient = RestClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openApiKey)
                .build();

        OpenAiEmbeddingResponse response = restClient
                .post()
                .uri("/embeddings")
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    System.err.println("OpenAI API Error: " + new String(res.getBody().readAllBytes()));
                })
                .body(OpenAiEmbeddingResponse.class);

        if (response == null || response.data() == null) {
            return Collections.emptyList();
        }

        return response.data().stream()
                .sorted(Comparator.comparingInt(OpenAiEmbeddingResponse.EmbeddingData::index))
                .map(OpenAiEmbeddingResponse.EmbeddingData::embedding)
                .toList();
    }


//    public List<List<Float>> getBatchEmbeddings(List<String> texts) {
//        OpenAiEmbeddingRequest request = new OpenAiEmbeddingRequest("text-embedding-3-small", texts);
//
//        RestClient restClient = RestClient.builder()
//                .baseUrl("https://api.openai.com/v1")
//                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openApiKey)
//                .build();
//
//        OpenAiEmbeddingResponse response = restClient.post()
//                .uri("/embeddings")
//                .body(request)
//                .retrieve()
//                .body(OpenAiEmbeddingResponse.class);
//
//        // 응답 데이터에서 임베딩 리스트만 추출하여 반환 (index 순서로 정렬 보장)
//        return response.data().stream()
//                .sorted(Comparator.comparingInt(OpenAiEmbeddingResponse.EmbeddingData::index))
//                .map(OpenAiEmbeddingResponse.EmbeddingData::embedding)
//                .toList();
//    }

    // 엘라스틱서치 라이브러리를 사용하니 Float 형이 직렬화되는 문제가 발생하는 것으로 추정
    // 라이브러리는 **"OpenSearch 방식(필드명을 키로 쓰는 방식)"**으로 JSON을 만들려고 시도하는데, 정작 내부 역직렬화(Deserialization) 규칙은 **"Elasticsearch 방식(field라는 키를 따로 쓰는 방식)"**을 기대하고 있어서 "왜 내가 모르는 필드명(item_vector)이 튀어나오냐"며 스스로 당황하는 상태입니다.
    //이런 라이브러리 내부의 규격 충돌은 빌더를 포기하고 _custom 쿼리로 완전히 수동 제어
    public List<FacilityRecommendResponse> getFacilitiesWithKNN(List<Float> queryVector) throws IOException {
        // 1. OpenSearch가 기대하는 '필드명 중심'의 구조를 직접 Map으로 생성
        // { "item_vector": { "vector": [...], "k": 5 } }
        Map<String, Object> knnBody = new HashMap<>();
        knnBody.put("vector", queryVector);
        knnBody.put("k", 50);

        Map<String, Object> knnField = new HashMap<>();
        knnField.put("item_vector", knnBody);

        // 2. _custom 메서드를 사용하여 'knn'이라는 이름의 쿼리로 전달
        SearchResponse<Map> response = elasticsearchClient.search(s -> s
                .index("item_recommendation_index")
                .size(5)
                .query(q -> q
                        ._custom("knn", knnField)
                ), Map.class
        );

        return response.hits().hits().stream()
                .map(hit -> {
                    Map<String, Object> source = (Map<String, Object>) hit.source();
                    return new FacilityRecommendResponse(
                            hit.id(),
                            String.valueOf(source.get("item_name")),
                            hit.score() != null ? hit.score() : 0.0);
                })
                .toList();
    }


//    public List<String> getFacilitiesWithKNN(List<Float> queryVector) throws IOException {
//        SearchResponse<Map> response = elasticsearchClient.search(s -> s
//                .index("item_recommendation_index")
//                .query(q -> q.knn(
//                        k -> k
//                                .field("item_vector")
//                                .queryVector(queryVector)
//                                .k(5)
//
//                )), Map.class
//        );
//        return response.hits().hits().stream()
//                .map(hit -> hit.source().get("item_name").toString())
//                .toList();
//    }

    /**
     * @param indexName facilities
     * @param pageSize 10MB 제한 -> 1536차원의 벡터데이터 -> Double 은 개당 8bite -> 백터 하나당 약 12KB -> pageSize가 1000이면 12MB 이므로 http 요청크기 10MB 제한을 넘어섬 300-~500
     */
    public void migrateToVectorIndex(String indexName, int pageSize, String lastId) throws IOException {
        List<FieldValue> lastSortValues = List.of(FieldValue.of(lastId));
        while (true) {
            final List<FieldValue> finalLastSortValues = lastSortValues;
            SearchResponse<Map> response = elasticsearchClient.search(s -> {
                        s.index(indexName)
                        .size(pageSize)
                        .sort(so -> so.field(f -> f.field("_id").order(SortOrder.Asc))); // 고유값 정렬 필수, search_after 를 써야 1만개 이상조회가능, 성능개선필요시 다른 number 로 정렬기준이 있어야
                if (finalLastSortValues != null) {
                    s.searchAfter(finalLastSortValues);
                }
                return s;
            }, Map.class);

            List<Hit<Map>> hits = response.hits().hits();
            if (hits.isEmpty()) break;

            List<String> descriptions = hits.stream()
                    .map(hit -> {
                        Map<?,?> source = hit.source();
                        return String.format(
                                "시설명은 %s이며, %s에 위치한 %s입니다. 정원은 %s명이고 설립연도는 %s년입니다. 등급은 %s입니다.",
                                source.get("name"),
                                source.get("address"),
                                source.get("facilityType"),
                                source.get("capacity"),
                                source.get("establishmentYear"),
                                source.get("grade")

                        );
                    })
                    .toList();

            List<List<Float>> allVectors = getBatchEmbeddings(descriptions);

            BulkRequest.Builder br = new BulkRequest.Builder();
            for (int i = 0; i < hits.size(); i++) {
                Hit<Map> hit = hits.get(i);
                List<Float> vector = allVectors.get(i);

                br.operations(op -> op.index(idx -> idx
                        .index("item_recommendation_index")
                        .id(hit.id())
                        .document(Map.of(
                                "item_name", hit.source().get("name"),
                                "item_vector", vector
                        ))
                ));
            }
            elasticsearchClient.bulk(br.build());

            lastSortValues = hits.get(hits.size() - 1).sort();
            log.info("processed = {}", hits.size());

            try { Thread.sleep(200); } catch (InterruptedException e) {}
        }

    }

}
