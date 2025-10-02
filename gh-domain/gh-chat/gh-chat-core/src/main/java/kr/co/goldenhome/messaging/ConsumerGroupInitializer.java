package kr.co.goldenhome.messaging;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsumerGroupInitializer {

    private final StringRedisTemplate redisTemplate;
    private static final String CHAT_STREAM_KEY = "chat:room:stream"; // 공통 스트림 키
    private static final String CHAT_GROUP_NAME = "chat.group";

    @PostConstruct
    public void create() {
        String groupName = "chat.group";
        try {
            redisTemplate.opsForStream().createGroup(CHAT_STREAM_KEY, groupName);
        } catch (RedisSystemException e) {
            if (e.getCause() != null && e.getCause().getMessage().contains("BUSYGROUP")) {
                System.out.println("컨슈머 그룹 '" + CHAT_GROUP_NAME + "'는 이미 존재합니다.");
            } else {
                throw e;
            }
        }
    }
}
