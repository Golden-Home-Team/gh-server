package kr.co.goldenhome.authentication.dto;

public record ResetPasswordRequest(
        String loginId,
        String newPassword,
        String confirmPassword
) {
}
