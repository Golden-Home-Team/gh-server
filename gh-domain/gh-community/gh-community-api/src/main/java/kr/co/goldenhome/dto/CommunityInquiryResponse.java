package kr.co.goldenhome.dto;

import kr.co.goldenhome.entity.CommunityInquiry;
import kr.co.goldenhome.enums.CommunityInquiryStatus;
import kr.co.goldenhome.enums.CommunityInquiryType;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

public record CommunityInquiryResponse(
        Long id,
        Long facilityId,
        Long userId,
        LocalDate recordDate,
        String content,
        CommunityInquiryType type,
        CommunityInquiryStatus status,
        Boolean isUrgent
) {

    public static CommunityInquiryResponse from(CommunityInquiry communityInquiry) {
        return new CommunityInquiryResponse(
                communityInquiry.getId(),
                communityInquiry.getFacilityId(),
                communityInquiry.getUserId(),
                communityInquiry.getRecordDate(),
                communityInquiry.getContent(),
                communityInquiry.getType(),
                communityInquiry.getStatus(),
                communityInquiry.getIsUrgent()
        );
    }

    public static CommunityInquiryResponse convertContent(CommunityInquiry communityInquiry) {
        return new CommunityInquiryResponse(
                communityInquiry.getId(),
                communityInquiry.getFacilityId(),
                communityInquiry.getUserId(),
                communityInquiry.getRecordDate(),
                StringUtils.truncate(communityInquiry.getContent(), 50),
                communityInquiry.getType(),
                communityInquiry.getStatus(),
                communityInquiry.getIsUrgent()
        );
    }
}
