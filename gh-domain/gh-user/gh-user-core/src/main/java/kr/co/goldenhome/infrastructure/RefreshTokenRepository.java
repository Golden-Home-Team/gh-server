package kr.co.goldenhome.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Primary
@Qualifier(value = "refreshTokenRepository")
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository implements TokenRepository {

    private final StringRedisTemplate redisTemplate;
    private static final String REFRESH_TOKEN_PREFIX = "userId:";

    @Override
    public void save(String key, String token, Duration expirationDuration) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + key, token, expirationDuration);
    }

    @Override
    public String getByKey(String userId) {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + userId);
    }

    public void deleteByKey(String userId) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);
    }
}
