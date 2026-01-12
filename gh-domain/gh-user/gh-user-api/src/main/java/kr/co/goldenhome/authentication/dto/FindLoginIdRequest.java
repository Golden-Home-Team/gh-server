package kr.co.goldenhome.authentication.dto;

import jakarta.validation.constraints.NotBlank;

public record FindLoginIdRequest(
        @NotBlank String type,
        @NotBlank String contact,
        @NotBlank String verificationCode
) {
}
