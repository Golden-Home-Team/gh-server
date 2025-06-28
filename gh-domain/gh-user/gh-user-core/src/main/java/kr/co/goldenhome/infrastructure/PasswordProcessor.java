package kr.co.goldenhome.infrastructure;

public interface PasswordProcessor {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
