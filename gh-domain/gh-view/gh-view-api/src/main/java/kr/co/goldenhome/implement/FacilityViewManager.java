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
public class FacilityViewManager {

    private final ElasticsearchOperations elasticsearchOperations;

    @DeduplicateEvent
    public void processViewEvent(FacilityEvent event) {
        UpdateQuery updateQuery = UpdateQuery.builder(event.getFacilityId().toString())
                .withScript("ctx._source.viewCount += 1")
                .withScriptType(ScriptType.INLINE)
                .build();
        elasticsearchOperations.update(updateQuery, IndexCoordinates.of("facilities"));
    }

}
