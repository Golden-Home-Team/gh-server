package kr.co.goldenhome.dto;

import kr.co.goldenhome.enums.ChatRoomType;

import java.time.LocalDateTime;

public record ChatRoomMetadataRepositoryResponse(
        Long chatRoomId,
        Long facilityId,
        ChatRoomType chatRoomType,
        LocalDateTime createdAt,
        String lastMessage,
        String timestamp
) {
}
