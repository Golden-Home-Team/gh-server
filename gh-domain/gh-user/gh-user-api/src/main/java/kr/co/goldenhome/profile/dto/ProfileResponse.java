package kr.co.goldenhome.profile.dto;

import kr.co.goldenhome.ProfileImageApiResponse;

public record ProfileResponse(ProfileImageApiResponse profileImageApiResponse, String name, String loginId, String phoneNumber, String email) {
}
