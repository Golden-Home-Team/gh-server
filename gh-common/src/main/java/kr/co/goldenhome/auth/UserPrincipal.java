package kr.co.goldenhome.auth;

import java.security.Principal;

public record UserPrincipal(Long userId) implements Principal {

    @Override
    public String getName() {
        return String.valueOf(userId);
    }
}
