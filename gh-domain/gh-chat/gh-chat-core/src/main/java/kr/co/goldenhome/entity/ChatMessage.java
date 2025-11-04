package kr.co.goldenhome.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;
    @Indexed
    private Long chatRoomId;
    private Long senderId;
    private String content;
    private String timestamp;

    @Builder
    private ChatMessage(String id, Long chatRoomId, Long senderId, String content, String timestamp) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public static ChatMessage create(Long chatRoomId, Long senderId, String content) {
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .senderId(senderId)
                .content(content)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    public static ChatMessage convert(Map<String, String> map) {
        return ChatMessage.builder()
                .chatRoomId(Long.valueOf(map.get("chatRoomId")))
                .senderId(Long.valueOf(map.get("senderId")))
                .content(map.get("content"))
                .timestamp(map.get("timestamp"))
                .build();
    }

}
