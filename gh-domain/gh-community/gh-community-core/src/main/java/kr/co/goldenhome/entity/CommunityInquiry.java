package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.enums.CommunityInquiryStatus;
import kr.co.goldenhome.enums.CommunityInquiryType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(
        name = "community_inquiries",
        indexes = {
                @Index(name = "idx_community_inquiries_facility_id", columnList = "facility_id")
        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long facilityId;
    private Long userId;
    private LocalDate recordDate;
    private String content; // 최대 300자
    @Column(name = "community_inquiry_type")
    @Enumerated(value = EnumType.STRING)
    private CommunityInquiryType type;
    @Column(name = "community_inquiry_status")
    @Enumerated(value = EnumType.STRING)
    private CommunityInquiryStatus status;
    private Boolean isUrgent;

    @Builder
    private CommunityInquiry(Long id, Long facilityId, Long userId, LocalDate recordDate, String content, CommunityInquiryType type, CommunityInquiryStatus status, Boolean isUrgent) {
        this.id = id;
        this.facilityId = facilityId;
        this.userId = userId;
        this.recordDate = recordDate;
        this.content = content;
        this.type = type;
        this.status = status;
        this.isUrgent = isUrgent;
    }

    public static CommunityInquiry create(Long facilityId, Long userId, LocalDate recordDate, String content, CommunityInquiryType type, Boolean isUrgent) {
        return CommunityInquiry.builder()
                .facilityId(facilityId)
                .userId(userId)
                .recordDate(recordDate)
                .content(content)
                .type(type)
                .status(CommunityInquiryStatus.PENDING)
                .isUrgent(isUrgent)
                .build();
    }
}
