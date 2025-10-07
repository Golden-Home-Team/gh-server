package kr.co.goldenhome.repository;

import kr.co.goldenhome.dto.ChatRoomMetadataRepositoryResponse;
import kr.co.goldenhome.entity.ChatRoom;
import kr.co.goldenhome.enums.ChatRoomType;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRoomRepositoryCustom {
    ChatRoom getPrivateChatRoom(Long userId, Long communityManagerUserId, Long facilityId, ChatRoomType chatRoomType);
    List<ChatRoomMetadataRepositoryResponse> getMyChatRooms(Long userId, LocalDateTime cursor);
}
