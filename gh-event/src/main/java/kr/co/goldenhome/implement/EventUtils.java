package kr.co.goldenhome.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.goldenhome.FacilityEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public final class EventUtils {

    private final ObjectMapper objectMapper;

    public PublishRequest createPublishRequest(String topicArn, Object event) throws JsonProcessingException {
        return PublishRequest.builder()
                .topicArn(topicArn)
                .message(objectMapper.writeValueAsString(event))
                .build();
    }

    public String toJson(Object event) throws JsonProcessingException {
        return objectMapper.writeValueAsString(event);
    }

    public FacilityEvent fromJson(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, FacilityEvent.class);
    }
}
