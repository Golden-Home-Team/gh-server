package kr.co.goldenhome;

import java.time.LocalDateTime;

public record DailyShotImageApiResponse(Long id, String imageUrl, LocalDateTime createdAt) {
}
