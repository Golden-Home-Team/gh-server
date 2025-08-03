package kr.co.goldenhome.authentication.dto;

import java.time.LocalDateTime;

public record VerificationConfirmResponse(LocalDateTime createdAt, String loginId, String resetPasswordToken) {

    public static VerificationConfirmResponse of(VerificationConfirmServiceResponse response, String resetPasswordToken) {
        return new VerificationConfirmResponse(response.createdAt(), response.loginId(), resetPasswordToken);
    }

    public static VerificationConfirmResponse from(VerificationConfirmServiceResponse response) {
        return new VerificationConfirmResponse(response.createdAt(), response.loginId(), null);
    }
}
