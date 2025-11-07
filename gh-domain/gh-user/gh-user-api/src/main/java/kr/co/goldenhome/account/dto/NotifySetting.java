package kr.co.goldenhome.account.dto;

import jakarta.validation.constraints.NotNull;

public record NotifySetting(
        @NotNull Boolean notice,
        @NotNull Boolean chat
) {
}
