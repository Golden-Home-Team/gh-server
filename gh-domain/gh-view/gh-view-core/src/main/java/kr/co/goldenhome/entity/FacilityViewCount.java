package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "facility_view_counts")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacilityViewCount {

    @Id
    private Long facilityId;
    private Long viewCount;

    @Builder
    private FacilityViewCount(Long facilityId, Long viewCount) {
        this.facilityId = facilityId;
        this.viewCount = viewCount;
    }

    public static FacilityViewCount create(Long facilityId, Long viewCount) {
        return FacilityViewCount.builder()
                .facilityId(facilityId)
                .viewCount(viewCount)
                .build();
    }
}
