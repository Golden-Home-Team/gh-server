package kr.co.goldenhome.dto;

import jakarta.validation.constraints.NotBlank;

public record CommunityNoticeRequest(@NotBlank String title, @NotBlank String content) {
}
