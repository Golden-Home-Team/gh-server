package kr.co.goldenhome.infrastructure;

import java.time.Duration;

public interface TokenRepository {

    void save(String key, String token, Duration expirationDuration);
    String getByKey(String key);
    void deleteByKey(String key);
}
