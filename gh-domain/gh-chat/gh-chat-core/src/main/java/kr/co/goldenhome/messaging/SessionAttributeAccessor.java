package kr.co.goldenhome.messaging;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SessionAttributeAccessor {

    public void updateSession(StompHeaderAccessor accessor, String key, Long id) {
        Objects.requireNonNull(accessor.getSessionAttributes()).put(key, id);
    }

    public Long getById(StompHeaderAccessor accessor, String key) {
        return (Long) Objects.requireNonNull(accessor.getSessionAttributes()).get(key);
    }
}
