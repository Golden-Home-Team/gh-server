package kr.co.goldenhome.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class FacilityViewDistributedLockRepository {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_FORMAT = "view::facility::%s::user::%s::lock";

    public boolean lock(Long facilityId, Long userId, Duration ttl) {
        String key = generateKey(facilityId, userId);
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, "", ttl));
    }

    private String generateKey(Long facilityId, Long userId) {
        return KEY_FORMAT.formatted(facilityId, userId);
    }
}
