package kr.co.goldenhome.authentication.dto;

import kr.co.goldenhome.enums.VerificationPurpose;

public record VerificationResponse(String verificationCode, VerificationPurpose purpose) {
}
