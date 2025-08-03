package kr.co.goldenhome.authentication.dto;

import java.time.LocalDateTime;


public record VerificationConfirmServiceResponse(
        LocalDateTime createdAt,
        String loginId) {
}
