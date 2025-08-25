package kr.co.goldenhome.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record DailyRehabilitationRequest(
        @NotNull LocalDate recordDate,
        String treatment,
        List<DailyExerciseRequest> dailyExerciseRequests
) {
}
