package kr.co.goldenhome.implement;

import kr.co.goldenhome.entity.ChatRoom;
import kr.co.goldenhome.entity.ChatUser;
import kr.co.goldenhome.enums.ChatRoomType;
import kr.co.goldenhome.repository.ChatRoomRepository;
import kr.co.goldenhome.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatManager {

    private final ChatUserRepository chatUserRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public Long getOrCreate(Long facilityId, Long userId, Long communityManagerUserId) {

        ChatRoom chatRoom = ChatRoom.create(facilityId, ChatRoomType.DIRECT_MESSAGE);
        Long chatRoomId = chatRoomRepository.save(chatRoom).getId();
        ChatUser chatUser1 = ChatUser.create(userId, chatRoomId);
        ChatUser chatUser2 = ChatUser.create(communityManagerUserId, chatRoomId);
        chatUserRepository.saveAll(List.of(chatUser1, chatUser2));
        return chatRoomId;
    }
}
