package kr.co.goldenhome.authentication.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPhoneNumberRequest(
        @NotBlank String type,
        @NotBlank String contact,
        @NotBlank String verificationCode,
        @NotBlank String phoneNumber
) {
}
