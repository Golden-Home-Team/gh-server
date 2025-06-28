package kr.co.goldenhome;

import kr.co.goldenhome.infrastructure.PasswordProcessor;

public class SpyPasswordProcessor implements PasswordProcessor {
    @Override
    public String encode(String rawPassword) {
        return "encodedPassword";
    }

    @Override
    public Boolean matches(String rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }
}
