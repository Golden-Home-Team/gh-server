package kr.co.goldenhome.authentication.dto;

import jakarta.validation.constraints.NotBlank;

public record FcmRequest(
        @NotBlank String fcmToken,
        @NotBlank String deviceId
) {
}
