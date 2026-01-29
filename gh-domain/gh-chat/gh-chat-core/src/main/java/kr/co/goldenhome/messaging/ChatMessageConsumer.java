package kr.co.goldenhome.messaging;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.stereotype.Component;

import static kr.co.goldenhome.constant.RedisConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageConsumer {

    private final StringRedisTemplate redisTemplate;
    private final StreamMessageListenerContainer<String, MapRecord<String, String,String>> container;
    private final ChatMessageListener chatMessageListener;
    private Subscription subscription; // 컨테이너의 구독 상태를 나타내는 객체

    @PostConstruct
    public void init() {
        try {
            redisTemplate.opsForStream().createGroup(CHAT_MESSAGE_STREAM_KEY, CHAT_CONSUMER_GROUP);
        } catch (RedisSystemException e) {
            if (e.getCause() != null && e.getCause().getMessage().contains("BUSYGROUP")) {
                log.info("컨슈머 그룹 '" + CHAT_CONSUMER_NAME + "'는 이미 존재합니다.");
            } else {
                throw e;
            }
        }
        subscription = container.receive(
                Consumer.from(CHAT_CONSUMER_GROUP, CHAT_CONSUMER_NAME),
                StreamOffset.create(CHAT_MESSAGE_STREAM_KEY, ReadOffset.lastConsumed()),
                chatMessageListener
        );
    }

    @PreDestroy
    public void destroy() {
        if (subscription != null) subscription.cancel();
        if (container != null && container.isRunning()) container.stop();
    }

}
