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

    private final Map<String, Key> jwks = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public Key get(String kid, Supplier<JwkSet> supplier) {
        Key key = jwks.get(kid);
        if (key != null) return key;
        try {
            if (lock.tryLock(5, TimeUnit.SECONDS)) {
                try {
                    key = jwks.get(kid);
                    if (key != null) return key;
                    JwkSet jwkSet = supplier.get();
                    jwkSet.forEach(jwk -> jwks.put(jwk.getId(), jwk.toKey()));
                    return jwks.get(kid);
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        throw new SocialLoginException();
    }
}
