package kr.co.goldenhome.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import kr.co.goldenhome.implement.FacilityViewManager;
import kr.co.goldenhome.model.FacilityEvent;
import kr.co.goldenhome.event.SqsMessage;
import kr.co.goldenhome.model.FacilityEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FacilityViewListener {

    private final FacilityViewManager facilityViewManager;
    private final ObjectMapper objectMapper;

    @SqsListener("gh-facility-view")
    public void updateElasticSearchViewCount(@Payload SqsMessage sqsMessage) throws JsonProcessingException {
        FacilityEvent facilityEvent = objectMapper.readValue(sqsMessage.Message(), FacilityEvent.class);
        if (FacilityEventType.VIEW != facilityEvent.getEventType()) return;
        facilityViewManager.processViewEvent(facilityEvent);
    }


}
