package kr.co.goldenhome.messaging;

import java.security.Principal;

public class ChatPrincipal implements Principal {

    private final Long userId;

    public ChatPrincipal(Long userId) {
        this.userId = userId;
    }
    @Override
    public String getName() {
        return String.valueOf(userId);
    }
}
