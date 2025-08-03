package kr.co.goldenhome.authentication.dto;

public record ResetPasswordRequest(
        String resetPasswordToken,
        String loginId,
        String newPassword,
        String confirmPassword
) {
}
