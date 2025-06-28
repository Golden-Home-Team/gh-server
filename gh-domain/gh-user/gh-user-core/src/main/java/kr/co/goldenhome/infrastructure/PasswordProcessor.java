package kr.co.goldenhome.infrastructure;

public interface PasswordProcessor {
    String encode(String rawPassword);
    Boolean matches(String rawPassword, String encodedPassword);
}
