package kr.co.goldenhome.authentication.dto;

public record VerificationConfirmRequest(String type, String contact, String verificationCode) {
}
