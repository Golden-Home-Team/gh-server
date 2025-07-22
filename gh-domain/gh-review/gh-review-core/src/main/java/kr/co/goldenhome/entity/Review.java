package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String content;
    @Column(columnDefinition = "integer default 5")
    private Integer score = 5;
    private LocalDateTime createdAt;

    @Builder
    private Review(Long id, Long facilityId, Long writerId, String content, Integer score, LocalDateTime createdAt) {
        this.id = id;
        this.facilityId = facilityId;
        this.writerId = writerId;
        this.content = content;
        this.score = score;
        this.createdAt = createdAt;
    }

    public static Review create(Long facilityId, Long writerId, String content, Integer score) {
        return Review.builder()
                .facilityId(facilityId)
                .writerId(writerId)
                .content(content)
                .score(score)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
