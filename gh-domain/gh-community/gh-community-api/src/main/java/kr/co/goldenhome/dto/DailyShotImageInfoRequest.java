package kr.co.goldenhome.dto;

public record DailyShotImageInfoRequest(
        String dailyShotType,
        String formattedImageName
) {
}
