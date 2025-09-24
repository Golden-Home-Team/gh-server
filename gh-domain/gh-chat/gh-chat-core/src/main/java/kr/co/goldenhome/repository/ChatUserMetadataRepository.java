package kr.co.goldenhome.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatUserMetadataRepository {

    private final StringRedisTemplate redisTemplate;
    private static final String CHAT_USER_METADATA_KEY_FORMAT = "chat_room::%s::user::%s"; // 키를 각각 관리하는 대신 해시를 사용해서 리소스 낭비를 줄임

    @SuppressWarnings("unchecked")
    public void connect(Long chatRoomId, Long chatUserId) {
        String key = CHAT_USER_METADATA_KEY_FORMAT.formatted(chatRoomId, chatUserId);
        redisTemplate.execute(new SessionCallback<List<Object>>() { // execute 메서드 내에 명령어는 큐에 쌓아두고 한번에 보냄
            @Override // 파이프라인 : 서버에 여러 개의 명령어를 한 번에 보내고, 그 결과를 한 번에 받는 기능, SessionCallback 고수준의 추상화로 기본메서드 RedisOperations 사용가능(단일 커넥션 보장 == 모든 명령어가 동일한 커넥션을 사용하도록 보장), RedisCallback 은 기본메서드를 못쓰고 직접 명령어를 써야할 때(직접 커넥션 접근, 단일 커넥션 보장 x)
            public <K, V> List<Object> execute(@NonNull RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi(); // 트랜잭션 시작
                operations.opsForHash().put((K) key, "isConnected", "true");
                operations.opsForHash().put((K) key, "unreadCount", "0");
                return operations.exec();
            }
        });
    }

    public void disconnect(Long chatRoomId, Long chatUserId) {
        String key = CHAT_USER_METADATA_KEY_FORMAT.formatted(chatRoomId, chatUserId);
        redisTemplate.opsForHash().put(key, "isConnected", "false");
    }

    public void unread(Long chatRoomId, Long chatUserId) {
        String key = CHAT_USER_METADATA_KEY_FORMAT.formatted(chatRoomId, chatUserId);
        redisTemplate.opsForHash().increment(key, "unreadCount", 1);
    }



}
