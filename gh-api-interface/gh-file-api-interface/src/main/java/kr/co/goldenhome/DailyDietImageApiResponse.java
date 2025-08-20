package kr.co.goldenhome;

import java.time.LocalDateTime;

public record DailyDietImageApiResponse(Long id, String dailyDietType, String imageUrl, LocalDateTime createdAt) {

}
