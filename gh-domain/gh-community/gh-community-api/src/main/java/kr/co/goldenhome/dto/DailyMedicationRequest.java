package kr.co.goldenhome.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DailyMedicationRequest(
        @NotNull LocalDate recordDate,
        String morningContent,
        String afternoonContent,
        String nightContent
) {
}
