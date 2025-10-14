package kr.co.goldenhome;

public record NotificationRequest(
        String token,
        String title,
        String body
) {
}
