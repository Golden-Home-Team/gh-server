package kr.co.goldenhome.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageListener implements StreamListener<String, MapRecord<String, String, String>> {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        String streamKey = message.getStream();
        String recordId = message.getId().toString();

        System.out.println("새로운 메시지 도착: " + message.getValue());

        // 예시: 메시지를 DB에 저장하거나 푸시 알림 보내기 등의 비즈니스 로직
        // ...

        // 메시지 처리가 완료되었음을 Redis에 알림 (XACK)
         redisTemplate.opsForStream().acknowledge(streamKey, "chat.group", recordId);
        // 또는 StreamMessageListenerContainer가 자동으로 ACK 처리하도록 설정할 수도 있습니다.
    }
}
