package kr.co.goldenhome.authentication.dto;

public record LoginResponse(String accessToken, String refreshToken) {
}
