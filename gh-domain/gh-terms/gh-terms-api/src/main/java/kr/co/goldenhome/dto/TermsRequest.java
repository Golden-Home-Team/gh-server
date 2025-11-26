package kr.co.goldenhome.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TermsRequest(
        @NotBlank String termsType,
        @NotBlank String version,
        @NotBlank String title,
        @NotNull String content,
        @NotNull Boolean isMandatory
) {
}
