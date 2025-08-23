package kr.co.goldenhome.dto;

import jakarta.validation.constraints.NotBlank;

public record FacilityProfileRequest(
        @NotBlank String formattedImageName
) {
}
