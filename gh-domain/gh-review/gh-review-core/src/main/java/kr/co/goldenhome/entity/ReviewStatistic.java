package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Table(name = "review_statistics")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewStatistic {

    @Id
    private Long facilityId;
    private Long count;
    private Long totalScore;
    @Column(precision = 3, scale = 2)
    private BigDecimal averageScore;
    @Column(name = "score1_count")
    private Long score1Count;
    @Column(name = "score2_count")
    private Long score2Count;
    @Column(name = "score3_count")
    private Long score3Count;
    @Column(name = "score4_count")
    private Long score4Count;
    @Column(name = "score5_count")
    private Long score5Count;
    @Version
    private Long version;

    @Builder
    private ReviewStatistic(Long facilityId, Long count, Long totalScore, BigDecimal averageScore, Long score1Count, Long score2Count, Long score3Count, Long score4Count, Long score5Count, Long version) {
        this.facilityId = facilityId;
        this.count = count;
        this.totalScore = totalScore;
        this.averageScore = averageScore;
        this.score1Count = score1Count;
        this.score2Count = score2Count;
        this.score3Count = score3Count;
        this.score4Count = score4Count;
        this.score5Count = score5Count;
        this.version = version;
    }

    public static ReviewStatistic create(Long facilityId, Long count, Long totalScore, BigDecimal averageScore) {
        return ReviewStatistic.builder()
                .facilityId(facilityId)
                .count(count)
                .totalScore(totalScore)
                .averageScore(averageScore)
                .score1Count(0L)
                .score2Count(0L)
                .score3Count(0L)
                .score4Count(0L)
                .score5Count(0L)
                .build();
    }

    public void append(int score) {
        this.count += 1;
        this.totalScore += score;
        BigDecimal total = BigDecimal.valueOf(this.totalScore);
        BigDecimal divisor = BigDecimal.valueOf(this.count);
        this.averageScore = total.divide(divisor, 2, RoundingMode.HALF_UP);
        switch (score) {
            case 1 -> score1Count++;
            case 2 -> score2Count++;
            case 3 -> score3Count++;
            case 4 -> score4Count++;
            case 5 -> score5Count++;
            default -> throw new IllegalArgumentException("점수는 1~5점 사이여야 합니다.");
        }
    }
}
