package kr.co.goldenhome.implement;

import kr.co.goldenhome.DeduplicateEvent;
import kr.co.goldenhome.model.FacilityEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.ScriptType;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReviewMetadataUpdater {

    private final ElasticsearchOperations elasticsearchOperations;

    @DeduplicateEvent
    public void processReviewEvent(FacilityEvent event) {
        String script =
                "ctx._source.reviewCount += 1; " +
                "ctx._source['avgScore'] = (float)params.newAverageScore;";
        UpdateQuery updateQuery = UpdateQuery.builder(event.getFacilityId().toString())
                .withScript(script)
                .withParams(Map.of("newAverageScore", event.getAvgScore()))
                .withScriptType(ScriptType.INLINE)
                .build();
        elasticsearchOperations.update(updateQuery, IndexCoordinates.of("facilities"));
    }
}
