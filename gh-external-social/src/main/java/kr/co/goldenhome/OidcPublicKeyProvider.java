package kr.co.goldenhome;

import io.jsonwebtoken.lang.Supplier;
import io.jsonwebtoken.security.JwkSet;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class OidcPublicKeyProvider {

    private final Map<String, Key> cache = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public Key get(String id, Supplier<JwkSet> supplier) {
        Key key = cache.get(id);
        if (key != null) return key;
        try {
            if (lock.tryLock(5, TimeUnit.SECONDS)) {
                try {
                    key = cache.get(id);
                    if (key != null) return key;
                    JwkSet jwkSet = supplier.get();
                    jwkSet.forEach(jwk -> cache.put(jwk.getId(), jwk.toKey()));
                    return cache.get(id);
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        throw new IllegalArgumentException();
    }
}
