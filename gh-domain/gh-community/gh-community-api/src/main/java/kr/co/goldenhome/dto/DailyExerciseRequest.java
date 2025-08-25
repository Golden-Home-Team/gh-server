package kr.co.goldenhome.dto;

import java.time.LocalTime;

public record DailyExerciseRequest(
        String content,
        LocalTime startTime,
        LocalTime endTime
) {
}
