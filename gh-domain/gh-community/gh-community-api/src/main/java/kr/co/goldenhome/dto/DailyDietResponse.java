package kr.co.goldenhome.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DailyDietResponse(
        String content,
        LocalDate recordDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<DailyDietImageResponse> dailyDietImageResponses
) {

}
