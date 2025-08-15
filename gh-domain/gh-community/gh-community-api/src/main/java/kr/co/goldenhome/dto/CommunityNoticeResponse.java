package kr.co.goldenhome.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.goldenhome.entity.CommunityNotice;

import java.time.LocalDateTime;

public record CommunityNoticeResponse(
        Long id,
        String title,
        String content,
        @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
        LocalDateTime createdAt) {
    public static CommunityNoticeResponse create(CommunityNotice communityNotice) {
        return new CommunityNoticeResponse(communityNotice.getId(), communityNotice.getTitle(), communityNotice.getContent(), communityNotice.getCreatedAt());
    }
}
