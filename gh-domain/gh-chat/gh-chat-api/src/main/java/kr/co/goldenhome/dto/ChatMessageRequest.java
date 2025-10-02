package kr.co.goldenhome.dto;

public record ChatMessageRequest(
        Long chatRoomId,
        String content
) {
}
