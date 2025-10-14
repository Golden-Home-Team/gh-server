package kr.co.goldenhome.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ChatConnectionRepository {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_FORMAT = "user::%s::session::%s::active_chat";
    private static final String ALL_SESSIONS_PATTERN = "user::%s::session::*::active_chat";


    public void connect(Long userId, String sessionId, Long chatRoomId, Duration ttl) {
        String key = generateKey(userId, sessionId);
        redisTemplate.opsForValue().set(key, String.valueOf(chatRoomId) ,ttl);
    }

    public void disconnect(Long userId, String sessionId) {
        String key = generateKey(userId, sessionId);
        redisTemplate.delete(key);
    }

    public boolean isUserViewingChatRoom(Long userId, Long chatRoomId) {
        String pattern = ALL_SESSIONS_PATTERN.formatted(userId);
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.isEmpty()) return false;
        String targetRoomId = String.valueOf(chatRoomId);
        for (String key : keys) {
            String activeRoomId = redisTemplate.opsForValue().get(key);
            if (targetRoomId.equals(activeRoomId)) return true;
        }
        return false;
    }

    private String generateKey(Long userId, String sessionId) {
        return KEY_FORMAT.formatted(userId, sessionId);
    }

}
