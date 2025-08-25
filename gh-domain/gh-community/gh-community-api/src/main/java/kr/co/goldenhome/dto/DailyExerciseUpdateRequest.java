package kr.co.goldenhome.dto;

import java.time.LocalTime;

public record DailyExerciseUpdateRequest(
        Long dailyExerciseId,
        String content,
        LocalTime startTime,
        LocalTime endTime
) {
}
