package kr.co.goldenhome.dto;

import java.util.List;

public record DailyRehabilitationInfo(
        Long dailyRehabilitationId,
        String treatment,
        List<DailyExerciseResponse> dailyExerciseResponses
) {
}
