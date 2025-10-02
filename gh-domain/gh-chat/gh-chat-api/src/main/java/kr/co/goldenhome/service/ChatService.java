package kr.co.goldenhome.service;

import kr.co.goldenhome.entity.ChatMessage;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.messaging.ChatMessageProducer;
import kr.co.goldenhome.messaging.ChatRoomCreatedEvent;
import kr.co.goldenhome.CommunityApi;
import kr.co.goldenhome.implement.ChatManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final CommunityApi communityApi;
    private final ChatManager chatManager;
    private final ChatMessageProducer chatMessageProducer;
    private final ApplicationEventPublisher eventPublisher;

    public Long enterRoom(Long facilityId, Long userId) {
        Long communityManagerUserId = communityApi.getCommunityManagerUserId(facilityId);
        if (Objects.equals(communityManagerUserId, userId)) throw new CustomException(ErrorCode.INVALID_CHAT_ROOM, "ChatService.enterRoom");
        Long chatRoomId = chatManager.getOrCreate(facilityId, userId, communityManagerUserId);
        eventPublisher.publishEvent(new ChatRoomCreatedEvent(this, chatRoomId));
        return chatRoomId;
    }

    public void chat(ChatMessage chatMessage) {

    }
}
