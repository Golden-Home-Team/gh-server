package kr.co.goldenhome.implement;

import kr.co.goldenhome.dto.ChatMessageResponse;
import kr.co.goldenhome.dto.ChatRoomMetadataRepositoryResponse;
import kr.co.goldenhome.dto.SliceResponse;
import kr.co.goldenhome.entity.ChatRoom;
import kr.co.goldenhome.entity.ChatUser;
import kr.co.goldenhome.enums.ChatRoomType;
import kr.co.goldenhome.repository.ChatRoomRepositoryCustom;
import kr.co.goldenhome.repository.ChatRoomRepository;
import kr.co.goldenhome.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatRoomManager {

    private final ChatRoomRepositoryCustom chatRoomRepositoryCustom;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserRepository chatUserRepository;

    @Transactional
    public ChatRoom getOrCreate(Long facilityId, Long userId, Long communityManagerUserId) {
        ChatRoom chatRoom = chatRoomRepositoryCustom.getPrivateChatRoom(userId, communityManagerUserId, facilityId, ChatRoomType.DIRECT_MESSAGE);
        if (chatRoom == null) {
            Long chatRoomId = chatRoomRepository.save(ChatRoom.create(facilityId, ChatRoomType.DIRECT_MESSAGE)).getId();
            chatUserRepository.saveAll(List.of(ChatUser.create(userId, chatRoomId), ChatUser.create(communityManagerUserId, chatRoomId)));
        }
        return chatRoom;
    }

    public SliceResponse<ChatRoomMetadataRepositoryResponse> getChatRooms(LocalDateTime cursor, Long userId, int pageSize) {
        return chatRoomRepositoryCustom.getChatRooms(cursor, userId, pageSize);
    }

    public SliceResponse<ChatMessageResponse> getChatMessages(Long chatRoomId, LocalDateTime cursor, Long userId, int pageSize) {
        return chatRoomRepositoryCustom.getChatMessages(chatRoomId, cursor, userId, pageSize);
    }
}
