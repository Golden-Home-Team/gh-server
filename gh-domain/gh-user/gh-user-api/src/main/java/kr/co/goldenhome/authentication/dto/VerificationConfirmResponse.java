package kr.co.goldenhome.authentication.dto;

import java.time.LocalDateTime;

public record VerificationConfirmResponse(LocalDateTime createdAt, String loginId) {
}
