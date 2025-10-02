package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.ChatRoom;
import kr.co.goldenhome.enums.ChatRoomType;

public interface ChatRoomRepositoryCustom {
    ChatRoom findPrivateChatRoom(Long userId, Long communityManagerUserId, Long facilityId, ChatRoomType chatRoomType);
}
