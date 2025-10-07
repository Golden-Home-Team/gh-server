package kr.co.goldenhome.messaging;

import kr.co.goldenhome.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

import static kr.co.goldenhome.constant.RedisConstants.CHAT_MESSAGE_STREAM_KEY;

@Service
@RequiredArgsConstructor
public class ChatStreamWriter {

    private final StringRedisTemplate redisTemplate;

    public void write(ChatMessage chatMessage) {
        Map<String, String> map = convertToMap(chatMessage);
        redisTemplate.opsForStream().add(CHAT_MESSAGE_STREAM_KEY, map);
    }

    private Map<String, String> convertToMap(ChatMessage chatMessage) {
        return Map.of(
                "chatRoomId", String.valueOf(chatMessage.getChatRoomId()),
                "senderId", String.valueOf(chatMessage.getSenderId()),
                "content", chatMessage.getContent(),
                "timestamp", chatMessage.getTimestamp()
        );
    }
}
