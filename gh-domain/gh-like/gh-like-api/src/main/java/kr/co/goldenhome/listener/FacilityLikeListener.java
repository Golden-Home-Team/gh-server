package kr.co.goldenhome.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import kr.co.goldenhome.event.SqsMessage;
import kr.co.goldenhome.implement.FacilityLikeManager;
import kr.co.goldenhome.model.FacilityEvent;
import kr.co.goldenhome.model.FacilityEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FacilityLikeListener {

    private final FacilityLikeManager facilityLikeManager;
    private final ObjectMapper objectMapper;

    @SqsListener("gh-facility-like")
    public void increaseElasticSearchLikeCount(@Payload SqsMessage sqsMessage) throws JsonProcessingException {
        FacilityEvent facilityEvent = objectMapper.readValue(sqsMessage.Message(), FacilityEvent.class);
        if (FacilityEventType.LIKE != facilityEvent.getEventType()) return;
        facilityLikeManager.processLikeEvent(facilityEvent);
    }

    @SqsListener("gh-facility-dislike")
    public void decreaseElasticSearchLikeCount(@Payload SqsMessage sqsMessage) throws JsonProcessingException {
        FacilityEvent facilityEvent = objectMapper.readValue(sqsMessage.Message(), FacilityEvent.class);
        if (FacilityEventType.DISLIKE != facilityEvent.getEventType()) return;
        facilityLikeManager.processDislikeEvent(facilityEvent);
    }


}
