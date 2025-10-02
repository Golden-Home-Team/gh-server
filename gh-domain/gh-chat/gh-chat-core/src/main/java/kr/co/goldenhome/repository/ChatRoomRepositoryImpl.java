package kr.co.goldenhome.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.goldenhome.entity.ChatRoom;
import kr.co.goldenhome.entity.QChatRoom;
import kr.co.goldenhome.entity.QChatUser;
import kr.co.goldenhome.enums.ChatRoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ChatRoom findPrivateChatRoom(Long userId, Long communityManagerUserId, Long facilityId, ChatRoomType chatRoomType) {
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
}
