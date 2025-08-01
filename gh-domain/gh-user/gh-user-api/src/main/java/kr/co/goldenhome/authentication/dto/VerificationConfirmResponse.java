package kr.co.goldenhome.authentication.dto;

import kr.co.goldenhome.enums.VerificationPurpose;

import java.time.LocalDateTime;

public record VerificationConfirmResponse(LocalDateTime createdAt, String loginId, VerificationPurpose purpose) {
}
