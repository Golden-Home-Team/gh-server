package kr.co.goldenhome.dto;



public record DailyMedicationUpdateRequest(
        String morningContent,
        String afternoonContent,
        String nightContent
) {
}
