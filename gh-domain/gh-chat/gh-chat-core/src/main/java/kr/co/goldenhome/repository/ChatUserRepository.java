package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    List<ChatUser> findByChatRoomId(Long chatRoomId);
}
