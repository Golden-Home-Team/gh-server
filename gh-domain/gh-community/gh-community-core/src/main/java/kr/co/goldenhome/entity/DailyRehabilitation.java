package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(
        name = "daily_rehabilitations",
        indexes = {
                @Index(name = "idx_daily_rehabilitations_facility_id_record_date", columnList = "facility_id, record_date")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"record_date"})
        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyRehabilitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long facilityId;
    private LocalDate recordDate;
    private String treatment;
    private LocalDateTime createdAt;

    @Builder
    private DailyRehabilitation(Long id, Long facilityId, LocalDate recordDate, String treatment, LocalDateTime createdAt) {
        this.id = id;
        this.facilityId = facilityId;
        this.recordDate = recordDate;
        this.treatment = treatment;
        this.createdAt = createdAt;
    }

    public static DailyRehabilitation create(Long facilityId, LocalDate recordDate, String treatment) {
        return DailyRehabilitation.builder()
                .facilityId(facilityId)
                .recordDate(recordDate)
                .treatment(treatment)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void update(String treatment) {
        this.treatment = treatment;
    }
}
