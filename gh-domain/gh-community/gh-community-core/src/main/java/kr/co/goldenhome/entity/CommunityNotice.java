package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        name = "community_notices",
        indexes = {
                @Index(name = "idx_community_notices_facility_id", columnList = "facility_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long facilityId;
    private String title;
    private String content;
    private Long createdByUserId;
    private LocalDateTime createdAt;

    @Builder
    private CommunityNotice(Long id, Long facilityId, String title, String content, Long createdByUserId, LocalDateTime createdAt) {
        this.id = id;
        this.facilityId = facilityId;
        this.title = title;
        this.content = content;
        this.createdByUserId = createdByUserId;
        this.createdAt = createdAt;
    }

    public static CommunityNotice create(String title, String content, Long facilityId, Long createdByUserId) {
        return CommunityNotice.builder()
                .title(title)
                .facilityId(facilityId)
                .content(content)
                .createdByUserId(createdByUserId)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
