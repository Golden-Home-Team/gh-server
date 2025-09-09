package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "reviews", indexes = {
        @Index(name = "idx_reviews_facility_score_id", columnList = "facility_id, score DESC, id DESC"),
        @Index(name = "idx_reviews_facility_id_desc", columnList = "facility_id, id DESC")
})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long facilityId;
    private Long writerId;
    @Column(columnDefinition = "integer default 5")
    private Integer score = 5;
    private boolean hasPhoto = false;
    private String positive;
    private String negative;
    @Enumerated(EnumType.STRING)
    private VisitPurpose visitPurpose;
    private LocalDate visitedAt;
    private LocalDateTime createdAt;

    @Builder
    private Review(Long id, Long facilityId, Long writerId, Integer score, boolean hasPhoto, String positive, String negative, VisitPurpose visitPurpose, LocalDate visitedAt, LocalDateTime createdAt) {
        this.id = id;
        this.facilityId = facilityId;
        this.writerId = writerId;
        this.score = score;
        this.hasPhoto = hasPhoto;
        this.positive = positive;
        this.negative = negative;
        this.visitPurpose = visitPurpose;
        this.visitedAt = visitedAt;
        this.createdAt = createdAt;
    }

    public static Review create(Long facilityId, Long writerId, Integer score, boolean hasPhoto, String positive, String negative, VisitPurpose visitPurpose, LocalDate visitedAt) {
        return Review.builder()
                .facilityId(facilityId)
                .writerId(writerId)
                .score(score)
                .hasPhoto(hasPhoto)
                .positive(positive)
                .negative(negative)
                .visitPurpose(visitPurpose)
                .visitedAt(visitedAt)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
