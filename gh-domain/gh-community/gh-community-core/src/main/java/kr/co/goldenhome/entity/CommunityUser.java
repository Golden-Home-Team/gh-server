package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.enums.CommunityUserRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(
        name = "community_users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"facility_id", "user_id"})
        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long facilityId;
    private Long userId;
    @Column(name = "community_user_role")
    @Enumerated(EnumType.STRING)
    private CommunityUserRole role;
    private LocalDateTime joinedAt;
    private LocalDateTime updatedAt;

    @Builder
    private CommunityUser(Long id, Long facilityId, Long userId, CommunityUserRole role, LocalDateTime joinedAt, LocalDateTime updatedAt) {
        this.id = id;
        this.facilityId = facilityId;
        this.userId = userId;
        this.role = role;
        this.joinedAt = joinedAt;
        this.updatedAt = updatedAt;
    }

    public static CommunityUser create(Long facilityId, Long userId, CommunityUserRole role) {
        return CommunityUser.builder()
                .facilityId(facilityId)
                .userId(userId)
                .role(role)
                .joinedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
