package kr.co.goldenhome.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import kr.co.goldenhome.event.SqsMessage;
import kr.co.goldenhome.implement.ReviewManager;
import kr.co.goldenhome.model.FacilityEvent;
import kr.co.goldenhome.model.FacilityEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewListener {

    private final ReviewManager reviewManager;
    private final ObjectMapper objectMapper;

    @SqsListener("gh-facility-review")
    public void increaseElasticSearchReviewCount(@Payload SqsMessage sqsMessage) throws JsonProcessingException {
        FacilityEvent facilityEvent = objectMapper.readValue(sqsMessage.Message(), FacilityEvent.class);
        if (FacilityEventType.REVIEW != facilityEvent.getEventType()) return;
        reviewManager.processReviewEvent(facilityEvent);
    }
}
