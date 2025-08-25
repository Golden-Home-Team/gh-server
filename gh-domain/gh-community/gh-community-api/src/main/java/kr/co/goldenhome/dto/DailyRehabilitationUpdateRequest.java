package kr.co.goldenhome.dto;

import java.util.List;

public record DailyRehabilitationUpdateRequest(
        String treatment,
        List<DailyExerciseUpdateRequest> dailyExerciseUpdateRequests
) {
}
