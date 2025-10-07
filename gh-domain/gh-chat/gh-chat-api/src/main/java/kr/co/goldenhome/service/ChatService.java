package kr.co.goldenhome.service;

import kr.co.goldenhome.dto.ChatRoomListResponse;
import kr.co.goldenhome.dto.SliceResponse;
import kr.co.goldenhome.entity.ChatMessage;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.messaging.ChatMessagePublisher;
import kr.co.goldenhome.messaging.ChatStreamWriter;
import kr.co.goldenhome.CommunityApi;
import kr.co.goldenhome.implement.ChatRoomManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final CommunityApi communityApi;
    private final ChatRoomManager chatRoomManager;
    private final ChatStreamWriter chatStreamWriter;
    private final ChatMessagePublisher chatMessagePublisher;

    public Long enterRoom(Long facilityId, Long userId) {
        Long communityManagerUserId = communityApi.getCommunityManagerUserId(facilityId);
        if (Objects.equals(communityManagerUserId, userId)) throw new CustomException(ErrorCode.INVALID_CHAT_ROOM, "ChatService.enterRoom");
        return chatRoomManager.getOrCreate(facilityId, userId, communityManagerUserId);
    }

    public void chat(ChatMessage chatMessage) {
        chatStreamWriter.write(chatMessage);
        chatMessagePublisher.sendChatMessage(chatMessage);
    }

    public SliceResponse<ChatRoomListResponse> getChatRooms(LocalDateTime cursor, Long userId) {
//        chatRoomManager.getMyChatRooms(userId);
        return null;
    }
}
