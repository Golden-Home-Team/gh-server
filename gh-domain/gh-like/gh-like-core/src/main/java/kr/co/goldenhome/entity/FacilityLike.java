package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Table(name = "facility_likes",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"facility_id", "user_id"})})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacilityLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long facilityId;
    private Long userId;
    private LocalDateTime createdAt;

    @Builder
    private FacilityLike(Long id, Long facilityId, Long userId, LocalDateTime createdAt) {
        this.id = id;
        this.facilityId = facilityId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public static FacilityLike create(Long facilityId, Long userId) {
        return FacilityLike.builder()
                .facilityId(facilityId)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
