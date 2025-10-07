package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findFirstByChatRoomIdInOrderByTimestampDesc(List<Long> chatRoomIds);
}
