package kr.co.goldenhome.dto;

import java.time.LocalTime;

public record DailyExerciseResponse(
        Long id,
        String content,
        LocalTime startTime,
        LocalTime endTime
) {
}
