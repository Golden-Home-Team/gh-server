package kr.co.goldenhome.implement;

import kr.co.goldenhome.entity.ChatRoom;
import kr.co.goldenhome.entity.ChatUser;
import kr.co.goldenhome.enums.ChatRoomType;
import kr.co.goldenhome.repository.ChatRoomRepositoryImpl;
import kr.co.goldenhome.repository.ChatRoomRepository;
import kr.co.goldenhome.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatRoomManager {

    private final ChatRoomRepositoryImpl chatRoomRepositoryImpl;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserRepository chatUserRepository;

    @Transactional
    public Long getOrCreate(Long facilityId, Long userId, Long communityManagerUserId) {
        ChatRoom chatRoom = chatRoomRepositoryImpl.getPrivateChatRoom(userId, communityManagerUserId, facilityId, ChatRoomType.DIRECT_MESSAGE);
        if (chatRoom == null) {
            Long chatRoomId = chatRoomRepository.save(ChatRoom.create(facilityId, ChatRoomType.DIRECT_MESSAGE)).getId();
            chatUserRepository.saveAll(List.of(ChatUser.create(userId, chatRoomId), ChatUser.create(communityManagerUserId, chatRoomId)));
            return chatRoomId;
        }
        return chatRoom.getId();
    }
}
