package kr.co.goldenhome.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatMessageProducer {

    private final StringRedisTemplate redisTemplate;

    public RecordId sendMessage(Long chatRoomId, Long userId, String message) {
        String streamKey = "chat:room:" + chatRoomId;

        // 1. 메시지 데이터를 Map 형태로 구성
        Map<String, String> messageData = new HashMap<>();
        messageData.put("userId", userId.toString());
        messageData.put("message", message);
        messageData.put("timestamp", String.valueOf(System.currentTimeMillis()));

        // 2. Redis Streams에 메시지 추가 (XADD 명령어)
        // Spring Data Redis가 RecordId를 자동으로 생성합니다.
        // streamKey, MapKey, MapValue
        MapRecord<String, String, String> record = StreamRecords.string(messageData).withStreamKey(streamKey);

        return redisTemplate.opsForStream().add(record);
    }
}
