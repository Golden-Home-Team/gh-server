package kr.co.goldenhome.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class FacilityViewCountRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String KEY_FORMAT = "view::facility::%s::view_count";
    private static final String RANKING_KEY = "view::facility::ranking";

    public Long read(Long facilityId) {
        String result = redisTemplate.opsForValue().get(generateKey(facilityId));
        return result == null ? 0L : Long.parseLong(result);
    }

    public Long increase(Long facilityId) {
        redisTemplate.opsForZSet().incrementScore(RANKING_KEY, String.valueOf(facilityId), 1);
        return redisTemplate.opsForValue().increment(generateKey(facilityId), 1);
    }

    public List<Long> getTopViewedFacilityIds(int page, int size) {
        int start = (page - 1) * size;
        int end = start + size - 1;

        Set<String> ids = redisTemplate.opsForZSet().reverseRange(RANKING_KEY, start, end);

        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        return ids.stream()
                .map(Long::valueOf)
                .toList();
    }

    private String generateKey(Long facilityId) {
        return KEY_FORMAT.formatted(facilityId);
    }
}
