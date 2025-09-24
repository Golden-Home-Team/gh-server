package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.enums.ChatRoomType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "chat_rooms")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long facilityId;
    private Boolean isDeleted;
    @Enumerated(EnumType.STRING)
    private ChatRoomType chatRoomType;
    private LocalDateTime createdAt;

    @Builder
    private ChatRoom(Long id, Long facilityId, Boolean isDeleted, ChatRoomType chatRoomType, LocalDateTime createdAt) {
        this.id = id;
        this.facilityId = facilityId;
        this.isDeleted = isDeleted;
        this.chatRoomType = chatRoomType;
        this.createdAt = createdAt;
    }

    public static ChatRoom create(Long facilityId, ChatRoomType chatRoomType) {
        return ChatRoom.builder()
                .facilityId(facilityId)
                .isDeleted(false)
                .chatRoomType(chatRoomType)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
