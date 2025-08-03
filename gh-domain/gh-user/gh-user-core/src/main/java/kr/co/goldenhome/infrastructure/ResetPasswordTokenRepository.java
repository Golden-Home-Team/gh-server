package kr.co.goldenhome.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

//@Primary
@Qualifier("resetPasswordTokenRepository")
@Repository
@RequiredArgsConstructor
public class ResetPasswordTokenRepository implements TokenRepository{
    private final StringRedisTemplate redisTemplate;
    private static final String RESET_PASSWORD = "reset-password:";

    @Override
    public void save(String key, String token, Duration expirationDuration) {
        redisTemplate.opsForValue().set(RESET_PASSWORD + key, token, expirationDuration);
    }

    @Override
    public String getByKey(String key) {
        return redisTemplate.opsForValue().get(RESET_PASSWORD + key);
    }
}
