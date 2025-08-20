package kr.co.goldenhome.dto;

import jakarta.validation.constraints.NotBlank;

public record CommunityNoticeUpdateRequest(@NotBlank String title, @NotBlank String content) {
}
