package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "facility_like_counts")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacilityLikeCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long facilityId;
    private long likeCount;

    @Builder
    private FacilityLikeCount(Long id, Long facilityId, long likeCount) {
        this.id = id;
        this.facilityId = facilityId;
        this.likeCount = likeCount;
    }

    public static FacilityLikeCount create(Long facilityId) {
        return FacilityLikeCount.builder()
                .facilityId(facilityId)
                .likeCount(1L)
                .build();
    }

}
