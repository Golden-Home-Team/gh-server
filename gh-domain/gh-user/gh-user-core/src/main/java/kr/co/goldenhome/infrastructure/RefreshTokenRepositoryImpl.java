package kr.co.goldenhome.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final StringRedisTemplate redisTemplate;
    private static final String REFRESH_TOKEN_PREFIX = "userId:";

    @Override
    public void save(Long key, String token, Duration expirationDuration) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX+ key, token, expirationDuration);
    }

    @Override
    public String getByUserId(String userId) {
        return "";
    }
}
