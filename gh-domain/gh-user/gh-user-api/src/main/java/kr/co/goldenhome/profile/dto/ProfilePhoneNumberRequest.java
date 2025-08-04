package kr.co.goldenhome.profile.dto;

import jakarta.validation.constraints.NotBlank;

public record ProfilePhoneNumberRequest(@NotBlank String phoneNumber) {
}
