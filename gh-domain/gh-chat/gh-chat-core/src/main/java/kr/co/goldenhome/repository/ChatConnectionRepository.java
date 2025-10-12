package kr.co.goldenhome.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class ChatConnectionRepository {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_FORMAT = "user::%s::session::%s::active_chat";

    public void connect(Long userId, String sessionId, Long chatRoomId, Duration ttl) {
        String key = generateKey(userId, sessionId);
        redisTemplate.opsForValue().set(key, String.valueOf(chatRoomId) ,ttl);
    }

    public void disconnect(Long userId, String sessionId) {
        String key = generateKey(userId, sessionId);
        redisTemplate.delete(key);
    }


    private String generateKey(Long userId, String sessionId) {
        return KEY_FORMAT.formatted(userId, sessionId);
    }

}
