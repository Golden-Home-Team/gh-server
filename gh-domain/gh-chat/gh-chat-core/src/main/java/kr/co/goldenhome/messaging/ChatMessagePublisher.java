package kr.co.goldenhome.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.goldenhome.entity.ChatMessage;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessagePublisher {

    private final StringRedisTemplate redisTemplate;
    private final ChannelTopic chatMessageTopic;
    private final ObjectMapper objectMapper;

    public void sendChatMessage(ChatMessage chatMessage) {
        try {
            redisTemplate.convertAndSend(chatMessageTopic.getTopic(), objectMapper.writeValueAsString(chatMessage));
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_PROCESSING_EXCEPTION, "ChatMessagePublisher.sendChatMessage");
        }
    }
}
