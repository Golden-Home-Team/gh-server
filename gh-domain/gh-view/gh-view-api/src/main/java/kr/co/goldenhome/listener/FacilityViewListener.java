package kr.co.goldenhome.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import kr.co.goldenhome.DeduplicateEvent;
import kr.co.goldenhome.FacilityEvent;
import kr.co.goldenhome.event.SqsMessage;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.ScriptType;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FacilityViewListener {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ObjectMapper objectMapper;

    @DeduplicateEvent
    @SqsListener("gh-facility-update-queue")
    public void updateElasticSearchViewCount(@Payload SqsMessage sqsMessage) throws JsonProcessingException {
        FacilityEvent facilityEvent = objectMapper.readValue(sqsMessage.Message(), FacilityEvent.class);
        if (facilityEvent.getEventId() == null) throw new CustomException(ErrorCode.INVALID_EVENT_PAYLOAD, "FacilityViewListener.view");
        UpdateQuery updateQuery = UpdateQuery.builder(facilityEvent.getFacilityId().toString())
                .withScript("ctx._source.viewCount += 1")
                .withScriptType(ScriptType.INLINE)
                .build();
        elasticsearchOperations.update(updateQuery, IndexCoordinates.of("facilities"));
    }
}
