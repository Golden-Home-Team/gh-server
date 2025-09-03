package kr.co.goldenhome.implement;

import kr.co.goldenhome.DeduplicateEvent;
import kr.co.goldenhome.model.FacilityEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.ScriptType;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FacilityLikeManager {

    private final ElasticsearchOperations elasticsearchOperations;

    @DeduplicateEvent
    public void processLikeEvent(FacilityEvent event) {
        UpdateQuery updateQuery = UpdateQuery.builder(event.getFacilityId().toString())
                .withScript("ctx._source.likeCount += 1")
                .withScriptType(ScriptType.INLINE)
                .build();
        elasticsearchOperations.update(updateQuery, IndexCoordinates.of("facilities"));
    }

    @DeduplicateEvent
    public void processDislikeEvent(FacilityEvent event) {
        UpdateQuery updateQuery = UpdateQuery.builder(event.getFacilityId().toString())
                .withScript("ctx._source.likeCount-= 1")
                .withScriptType(ScriptType.INLINE)
                .build();
        elasticsearchOperations.update(updateQuery, IndexCoordinates.of("facilities"));
    }

}
