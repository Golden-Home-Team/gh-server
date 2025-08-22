package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "facility_profiles")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacilityProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long facilityId;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private FacilityProfile(Long id, Long facilityId, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.facilityId = facilityId;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static FacilityProfile create(Long facilityId, String imageUrl) {
        return FacilityProfile.builder()
                .facilityId(facilityId)
                .imageUrl(imageUrl)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
