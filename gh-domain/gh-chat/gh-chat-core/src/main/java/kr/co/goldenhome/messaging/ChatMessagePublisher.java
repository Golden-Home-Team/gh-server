package kr.co.goldenhome.messaging;

import kr.co.goldenhome.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessagePublisher {

    private final StringRedisTemplate redisTemplate;
    private final ChannelTopic chatMessageTopic;

    public void sendChatMessage(ChatMessage chatMessage) {
        redisTemplate.convertAndSend(chatMessageTopic.getTopic(), chatMessage);
    }
}
