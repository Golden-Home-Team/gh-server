package kr.co.goldenhome.dto;

import java.time.LocalDateTime;

public record NoticeInfo(
        Long noticeId,
        String noticeTitle,
        String noticeContent,
        LocalDateTime noticeCreatedAt
) {
}
