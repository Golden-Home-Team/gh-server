package kr.co.goldenhome.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CommunityInquiryRequest(
        @NotNull LocalDate recordDate,
        @NotBlank String content,
        @NotBlank String type,
        boolean isUrgent
) {
}
