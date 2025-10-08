package kr.co.goldenhome.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.goldenhome.dto.ChatMessageResponse;
import kr.co.goldenhome.dto.ChatRoomMetadataRepositoryResponse;
import kr.co.goldenhome.dto.ChatRoomRepositoryResponse;
import kr.co.goldenhome.dto.SliceResponse;
import kr.co.goldenhome.entity.ChatMessage;
import kr.co.goldenhome.entity.ChatRoom;
import kr.co.goldenhome.entity.QChatRoom;
import kr.co.goldenhome.entity.QChatUser;
import kr.co.goldenhome.enums.ChatRoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final ChatMessageRepository chatMessageRepository;

    @Override
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
    public SliceResponse<ChatRoomMetadataRepositoryResponse> getChatRooms(LocalDateTime cursor, Long userId, int pageSize) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        QChatUser chatUser = QChatUser.chatUser;
        BooleanExpression cursorCondition = (cursor != null)
                ? chatRoom.createdAt.lt(cursor)
                : null;
        List<ChatRoomRepositoryResponse> chatRoomResponses = jpaQueryFactory
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
                        .and(cursorCondition)
                )
                .orderBy(chatRoom.createdAt.desc())
                .limit(pageSize+1)
                .fetch();
        List<Long> chatRoomIds = chatRoomResponses.stream()
                .map(ChatRoomRepositoryResponse::chatRoomId)
                .toList();
        List<ChatMessage> chatMessages = chatMessageRepository.findFirstByChatRoomIdInOrderByTimestampDesc(chatRoomIds);
        Map<Long, ChatMessage> latestMessagesMap = chatMessages.stream()
                .collect(Collectors.toMap(
                        ChatMessage::getChatRoomId,
                        Function.identity()
                ));

        List<ChatRoomMetadataRepositoryResponse> content = chatRoomResponses.stream()
                .map(roomResponse -> {
                    ChatMessage latestMessage = latestMessagesMap.get(roomResponse.chatRoomId());
                    return new ChatRoomMetadataRepositoryResponse(
                            roomResponse.chatRoomId(),
                            roomResponse.facilityId(),
                            roomResponse.chatRoomType(),
                            roomResponse.createdAt(),
                            latestMessage.getContent(),
                            latestMessage.getTimestamp());
                })
                .collect(Collectors.toList());

        boolean hasNext = content.size() > pageSize;
        LocalDateTime nextCursor = hasNext ? content.get(pageSize - 1).createdAt() : null;
        if (hasNext) {
            content.remove(pageSize);
        }
        return new SliceResponse<>(
                content,
                hasNext,
                content.size(),
                nextCursor
        );
    }

    @Override
    public SliceResponse<ChatMessageResponse> getChatMessages(Long chatRoomId, LocalDateTime cursor, Long userId, int pageSize) {
        Pageable pageable = PageRequest.ofSize(pageSize + 1);
        List<ChatMessage> chatMessages;
        if (cursor != null) chatMessages = chatMessageRepository.findByChatRoomIdAndTimestampBeforeOrderByTimestampDesc(chatRoomId, cursor.toString(), pageable);
        else chatMessages = chatMessageRepository.findByChatRoomIdOrderByTimestamp(chatRoomId, pageable);

        List<ChatMessageResponse> content = chatMessages.stream()
                .map(c -> {
                    boolean isMine = c.getSenderId().equals(userId);
                    return new ChatMessageResponse(
                            c.getSenderId(),
                            isMine,
                            c.getContent(),
                            c.getTimestamp()
                    );
                }).collect(Collectors.toList());
        boolean hasNext = content.size() > pageSize;
        LocalDateTime nextCursor = null;
        if (hasNext) {
            nextCursor = LocalDateTime.parse(content.get(pageSize-1).timestamp());
            content.remove(pageSize);
        }
        return new SliceResponse<>(
                content,
                hasNext,
                content.size(),
                nextCursor
        );
    }
}
