package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(
        name = "community_schedules",
        indexes = {
                @Index(name = "idx_community_schedules_facility_id_record_date", columnList = "facility_id, record_date")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"record_date"})
        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunitySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long facilityId;
    private LocalDate recordDate;
    private String content;

    @Builder
    private CommunitySchedule(Long id, Long facilityId, LocalDate recordDate, String content) {
        this.id = id;
        this.facilityId = facilityId;
        this.recordDate = recordDate;
        this.content = content;
    }

    public static CommunitySchedule create(Long facilityId, LocalDate recordDate, String content) {
        return CommunitySchedule.builder()
                .facilityId(facilityId)
                .recordDate(recordDate)
                .content(content)
                .build();
    }

    public void update(String content) {
        this.content = content;
    }
}
