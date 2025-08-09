package kr.co.goldenhome.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FacilityViewCountRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String KEY_FORMAT = "view::facility::%s::view_count";

    public Long read(Long facilityId) {
        String result = redisTemplate.opsForValue().get(generateKey(facilityId));
        return result == null ? 0L : Long.parseLong(result);
    }

    public Long increase(Long facilityId) {
        return redisTemplate.opsForValue().increment(generateKey(facilityId), 1);
    }

    private String generateKey(Long facilityId) {
        return KEY_FORMAT.formatted(facilityId);
    }
}
