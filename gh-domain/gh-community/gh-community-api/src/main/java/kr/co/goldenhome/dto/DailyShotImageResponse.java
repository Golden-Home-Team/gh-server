package kr.co.goldenhome.dto;

import java.time.LocalDateTime;

public record DailyShotImageResponse(Long id, String imageUrl, LocalDateTime createdAt) {

    public static DailyShotImageResponse empty() { return new DailyShotImageResponse(null, null, null); }
}
