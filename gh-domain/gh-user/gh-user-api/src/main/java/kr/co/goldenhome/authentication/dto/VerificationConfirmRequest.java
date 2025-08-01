package kr.co.goldenhome.authentication.dto;

import kr.co.goldenhome.enums.VerificationPurpose;

public record VerificationConfirmRequest(String type, String contact, String verificationCode, String purpose) {
}
