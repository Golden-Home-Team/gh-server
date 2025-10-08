package kr.co.goldenhome.repository;

import kr.co.goldenhome.dto.ChatMessageResponse;
import kr.co.goldenhome.dto.ChatRoomMetadataRepositoryResponse;
import kr.co.goldenhome.dto.SliceResponse;
import kr.co.goldenhome.entity.ChatRoom;
import kr.co.goldenhome.enums.ChatRoomType;

import java.time.LocalDateTime;

public interface ChatRoomRepositoryCustom {
    ChatRoom getPrivateChatRoom(Long userId, Long communityManagerUserId, Long facilityId, ChatRoomType chatRoomType);
    SliceResponse<ChatRoomMetadataRepositoryResponse> getChatRooms(LocalDateTime cursor, Long userId, int pageSize);
    SliceResponse<ChatMessageResponse> getChatMessages(Long chatRoomId,LocalDateTime cursor, Long userId, int pageSize);
}
