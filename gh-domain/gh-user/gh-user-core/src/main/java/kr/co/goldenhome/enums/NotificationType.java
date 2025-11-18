package kr.co.goldenhome.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum NotificationType {
    NOTICE("notice_topic"), CHAT("chat_topic");

    private final String fcmTopic;

}
