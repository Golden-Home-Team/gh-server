package kr.co.goldenhome.authentication.dto;

import java.time.LocalDateTime;

public record FindLoginIdResponse(
        LocalDateTime createdAt,
        String loginId
) {
}
