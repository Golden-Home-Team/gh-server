package kr.co.goldenhome.messaging;

import kr.co.goldenhome.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatStreamWriter {

    private final StringRedisTemplate redisTemplate;
    private static final String CHAT_MESSAGE_STREAM_NAME = "chat_message";

    public void write(ChatMessage chatMessage) {
        Map<String, String> map = convertToMap(chatMessage);
        redisTemplate.opsForStream().add(CHAT_MESSAGE_STREAM_NAME, map);
    }

    private Map<String, String> convertToMap(ChatMessage chatMessage) {
        return Map.of(
                "chatRoomId", String.valueOf(chatMessage.getChatRoomId()),
                "senderId", String.valueOf(chatMessage.getSenderId()),
                "content", chatMessage.getContent(),
                "createdAt", String.valueOf(chatMessage.getCreatedAt())
        );
    }
}
