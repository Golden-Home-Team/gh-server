package kr.co.goldenhome.dto;

public record DailyMedicationInfo(
        Long dailyMedicationId,
        String morningContent,
        String afternoonContent,
        String nightContent
) {
}
