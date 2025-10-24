package kr.co.goldenhome;

import java.util.List;

public record NotificationsRequest(
        List<String> token,
        String title,
        String body
) {
}
