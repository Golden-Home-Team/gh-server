package kr.co.goldenhome.dto;

public record DailyShotInfo(
        Long dailyShotId,
        String dailyShotContent,
        DailyShotImageResponse dailyShotImageResponse
) {
}
