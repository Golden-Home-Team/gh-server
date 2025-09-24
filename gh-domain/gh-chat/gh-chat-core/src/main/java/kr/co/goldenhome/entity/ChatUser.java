package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "chat_users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long chatRoomId;
    private Boolean isDeleted;
    private LocalDateTime createdAt;

    @Builder
    private ChatUser(Long id, Long userId, Long chatRoomId, Boolean isDeleted, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.chatRoomId = chatRoomId;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }

    public static ChatUser create(Long userId, Long chatRoomId) {
        return ChatUser.builder()
                .userId(userId)
                .chatRoomId(chatRoomId)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
