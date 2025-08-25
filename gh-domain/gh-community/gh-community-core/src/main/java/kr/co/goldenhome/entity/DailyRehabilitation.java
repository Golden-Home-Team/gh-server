package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(
        name = "daily_rehabilitations",
        indexes = {
                @Index(name = "idx_daily_rehabilitations_facility_id", columnList = "facility_id")
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

    @Builder
    private DailyRehabilitation(Long id, Long facilityId, LocalDate recordDate, String treatment) {
        this.id = id;
        this.facilityId = facilityId;
        this.recordDate = recordDate;
        this.treatment = treatment;
    }

    public static DailyRehabilitation create(Long facilityId, LocalDate recordDate, String treatment) {
        return DailyRehabilitation.builder()
                .facilityId(facilityId)
                .recordDate(recordDate)
                .treatment(treatment)
                .build();
    }

    public void update(String treatment) {
        this.treatment = treatment;
    }
}
