package kr.co.goldenhome.dto;

import kr.co.goldenhome.enums.ChatRoomType;

import java.time.LocalDateTime;

public record ChatRoomRepositoryResponse(
        Long chatRoomId,
        Long facilityId,
        ChatRoomType chatRoomType,
        LocalDateTime createdAt
) {


}
