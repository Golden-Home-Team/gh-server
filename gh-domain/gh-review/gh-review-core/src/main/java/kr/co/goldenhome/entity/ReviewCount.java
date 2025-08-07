package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "review_counts")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long facilityId;
    private Long reviewCount;

    @Builder
    private ReviewCount(Long id, Long facilityId, Long reviewCount) {
        this.id = id;
        this.facilityId = facilityId;
        this.reviewCount = reviewCount;
    }

    public static ReviewCount create(Long facilityId, Long reviewCount) {
        return ReviewCount.builder().facilityId(facilityId).reviewCount(reviewCount).build();
    }
}
