package kr.co.goldenhome.dto;

public record ChatMessageResponse(
        Long senderId,
        Boolean isMine,
        String message,
        String timestamp
) {
}
