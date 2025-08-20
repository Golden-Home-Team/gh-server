package kr.co.goldenhome.dto;

import java.time.LocalDateTime;

public record DailyDietImageResponse(Long id, String dailyDietType, String imageUrl, LocalDateTime createdAt) {

    public static DailyDietImageResponse empty() {
        return new DailyDietImageResponse(null, null, null, null);
    }
}
