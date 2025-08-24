package kr.co.goldenhome.dto;

import kr.co.goldenhome.entity.DailyMedication;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DailyMedicationResponse(
        Long id,
        LocalDate recordDate,
        String morningContent,
        String afternoonContent,
        String nightContent,
        LocalDateTime createdAt
) {
    public static DailyMedicationResponse from(DailyMedication dailyMedication) {
        return new DailyMedicationResponse(
                dailyMedication.getId(),
                dailyMedication.getRecordDate(),
                dailyMedication.getMorningContent(),
                dailyMedication.getAfternoonContent(),
                dailyMedication.getNightContent(),
                dailyMedication.getCreatedAt()
        );
    }
}
