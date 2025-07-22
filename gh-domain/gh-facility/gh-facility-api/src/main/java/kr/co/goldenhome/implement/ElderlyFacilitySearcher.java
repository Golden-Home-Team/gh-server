package kr.co.goldenhome.implement;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import kr.co.goldenhome.dto.ElderlyFacilityResponse;
import kr.co.goldenhome.entity.ElderlyFacility;
import kr.co.goldenhome.entity.ElderlyFacilityDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ElderlyFacilitySearcher {

    private final ElasticsearchOperations elasticsearchOperations;

    public List<ElderlyFacilityDocument> search(String query, String address, String facilityType, String grade, double minPrice, double maxPrice, int withinYears, int page, int size) {
        List<Query> queries = new ArrayList<>();
        List<Query> filters = new ArrayList<>();

        if (StringUtils.hasText(query)) {
            Query searchQuery = MatchQuery.of(m -> m
                    .query(query)
                    .field("name")
                    .fuzziness("AUTO")
            )._toQuery();
            queries.add(searchQuery);
        }
        if (StringUtils.hasText(address)) {
            Query addressQuery = MatchQuery.of(m -> m
                    .query(address)
                    .field("address")
            )._toQuery();
            queries.add(addressQuery);
        }


        if (StringUtils.hasText(facilityType)) {
            Query facilityTypeFilter = TermQuery.of(t -> t
                    .field("facilityType")
                    .value(facilityType)
            )._toQuery();
            filters.add(facilityTypeFilter);
        }

        if (StringUtils.hasText(grade)) {
            Query gradeFilter = TermQuery.of(t -> t
                    .field("grade")
                    .value(grade)
            )._toQuery();
            filters.add(gradeFilter);
        }

        Query priceRangeFilter = NumberRangeQuery.of(r -> r
                .field("price")
                .gte(minPrice)
                .lte(maxPrice)
        )._toRangeQuery()._toQuery();
        filters.add(priceRangeFilter);

        if (withinYears > 0) {
            double currentYear =  Year.now().getValue();
            Query yearRangeFilter = NumberRangeQuery.of(r -> r
                    .field("establishmentYear")
                    .gte(currentYear - withinYears)
                    .lte(currentYear)
            )._toRangeQuery()._toQuery();
            filters.add(yearRangeFilter);
        }

        Query boolQuery = BoolQuery.of(b -> b
                .must(queries)
                .filter(filters)
        )._toQuery();

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(page-1, size))
                .build();

        SearchHits<ElderlyFacilityDocument> searchHits = elasticsearchOperations.search(nativeQuery, ElderlyFacilityDocument.class);
        return searchHits.getSearchHits().stream()
                .map(hit -> hit.getContent()).toList();
    }

}
