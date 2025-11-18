package kr.co.goldenhome.account.dto;

import jakarta.validation.constraints.NotNull;

public record NotificationSettingRequest(
        @NotNull String type,
        @NotNull Boolean isEnabled
) {
}
