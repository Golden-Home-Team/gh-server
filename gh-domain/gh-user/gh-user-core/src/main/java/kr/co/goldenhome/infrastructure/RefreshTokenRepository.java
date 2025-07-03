package kr.co.goldenhome.infrastructure;

import java.time.Duration;

public interface RefreshTokenRepository {

    void save(Long key, String token, Duration expirationDuration);
    String getByUserId(Long userId);
}
