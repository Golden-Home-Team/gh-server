package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Table(
        name = "daily_exercises",
        indexes = {
                @Index(name = "idx_daily_exercises_daily_rehabilitation_id", columnList = "daily_rehabilitation_id")
        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long dailyRehabilitationId;
    private String content;
    private LocalTime startTime;
    private LocalTime endTime;

    @Builder
    private DailyExercise(Long id, Long dailyRehabilitationId, String content, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.dailyRehabilitationId = dailyRehabilitationId;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static DailyExercise create(Long dailyRehabilitationId, String content, LocalTime startTime, LocalTime endTime) {
        return DailyExercise.builder()
                .dailyRehabilitationId(dailyRehabilitationId)
                .content(content)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    public void update(String content, LocalTime startTime, LocalTime endTime) {
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
