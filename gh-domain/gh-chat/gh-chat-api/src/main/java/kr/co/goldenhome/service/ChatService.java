package kr.co.goldenhome.service;

import kr.co.goldenhome.entity.ChatMessage;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.messaging.ChatMessagePublisher;
import kr.co.goldenhome.messaging.ChatStreamWriter;
import kr.co.goldenhome.CommunityApi;
import kr.co.goldenhome.implement.ChatManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final CommunityApi communityApi;
    private final ChatManager chatManager;
    private final ChatStreamWriter chatStreamWriter;
    private final ChatMessagePublisher chatMessagePublisher;

    public Long enterRoom(Long facilityId, Long userId) {
        Long communityManagerUserId = communityApi.getCommunityManagerUserId(facilityId);
        if (Objects.equals(communityManagerUserId, userId)) throw new CustomException(ErrorCode.INVALID_CHAT_ROOM, "ChatService.enterRoom");
        return chatManager.getOrCreate(facilityId, userId, communityManagerUserId);
    }

    public void chat(ChatMessage chatMessage) {
        chatStreamWriter.write(chatMessage);
        chatMessagePublisher.sendChatMessage(chatMessage);
    }
}
