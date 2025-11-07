package kr.co.goldenhome.messaging;

public record ChatNotificationEvent(Long chatRoomId, Long senderId, String content) {

}
