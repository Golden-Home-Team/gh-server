package kr.co.goldenhome.profile.dto;

import jakarta.validation.constraints.NotBlank;

public record ProfileEmailRequest(@NotBlank String email) {
}
