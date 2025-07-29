package kr.co.goldenhome.authentication.dto;

public record RecoveryPasswordRequest(String LoginId, String type, String contact) {
}
