package kr.co.goldenhome.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.goldenhome.dto.ChatRoomMetadataRepositoryResponse;
import kr.co.goldenhome.dto.ChatRoomRepositoryResponse;
import kr.co.goldenhome.entity.ChatMessage;
import kr.co.goldenhome.entity.ChatRoom;
import kr.co.goldenhome.entity.QChatRoom;
import kr.co.goldenhome.entity.QChatUser;
import kr.co.goldenhome.enums.ChatRoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoom getPrivateChatRoom(Long userId, Long communityManagerUserId, Long facilityId, ChatRoomType chatRoomType) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        QChatUser chatUser = QChatUser.chatUser;
        return jpaQueryFactory
                .select(chatRoom)
                .from(chatRoom)
                .join(chatUser).on(chatUser.chatRoomId.eq(chatRoom.id))
                .where(chatRoom.facilityId.eq(facilityId)
                        .and(chatRoom.chatRoomType.eq(chatRoomType))
                        .and(chatUser.userId.in(userId, communityManagerUserId))
                )
                .groupBy(chatRoom.id)
                .having(chatUser.userId.count().eq(2L))
                .fetchOne();
    }

    @Override
    public List<ChatRoomMetadataRepositoryResponse> getMyChatRooms(Long userId, LocalDateTime cursor) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        QChatUser chatUser = QChatUser.chatUser;
        List<ChatRoomRepositoryResponse> responses = jpaQueryFactory
                .select(Projections.constructor(
                        ChatRoomRepositoryResponse.class,
                        chatRoom.id,
                        chatRoom.facilityId,
                        chatRoom.chatRoomType,
                        chatRoom.createdAt
                ))
                .from(chatUser)
                .join(chatRoom).on(chatUser.chatRoomId.eq(chatRoom.id))
                .where(chatUser.userId.eq(userId)
                        .and(chatRoom.isDeleted.isFalse())
                )
                .fetch();
        List<Long> chatRoomIds = responses.stream()
                .map(ChatRoomRepositoryResponse::chatRoomId)
                .toList();
        List<ChatMessage> chatMessages = chatMessageRepository.findFirstByChatRoomIdInOrderByTimestampDesc(chatRoomIds);
        return null;
    }
}
