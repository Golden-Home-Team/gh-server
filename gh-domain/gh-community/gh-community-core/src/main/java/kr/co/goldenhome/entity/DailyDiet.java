package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(
        name = "daily_diets",
        indexes = {
                @Index(name = "idx_daily_diets_facility_id", columnList = "facility_id")
        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyDiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long facilityId;
    private String content;
    private LocalDate recordDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private DailyDiet(Long id, Long facilityId, String content, LocalDate recordDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.facilityId = facilityId;
        this.content = content;
        this.recordDate = recordDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DailyDiet create(Long facilityId, String content,  LocalDate recordDate) {
        return DailyDiet.builder()
                .facilityId(facilityId)
                .content(content)
                .recordDate(recordDate)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
}
