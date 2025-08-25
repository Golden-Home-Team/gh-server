package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(
        name = "daily_medications",
        indexes = {
                @Index(name = "idx_daily_medications_facility_id_record_date", columnList = "facility_id, record_date")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"record_date"})
        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyMedication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long facilityId;
    private LocalDate recordDate;
    private String morningContent;
    private String afternoonContent;
    private String nightContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private DailyMedication(Long id, Long facilityId, LocalDate recordDate, String morningContent, String afternoonContent, String nightContent, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.facilityId = facilityId;
        this.recordDate = recordDate;
        this.morningContent = morningContent;
        this.afternoonContent = afternoonContent;
        this.nightContent = nightContent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DailyMedication create(Long facilityId, LocalDate recordDate, String morningContent, String afternoonContent, String nightContent) {
        return DailyMedication.builder()
                .facilityId(facilityId)
                .recordDate(recordDate)
                .morningContent(morningContent)
                .afternoonContent(afternoonContent)
                .nightContent(nightContent)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void update(String morningContent, String afternoonContent, String nightContent) {
        this.morningContent = morningContent;
        this.afternoonContent = afternoonContent;
        this.nightContent = nightContent;
        this.updatedAt = LocalDateTime.now();
    }

}
