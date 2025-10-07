package kr.co.goldenhome.dto;

public record ChatRoomListResponse(
        Long chatRoomId,
        String facilityName,
        String lastMessage,
        String timestamp
) {
}
