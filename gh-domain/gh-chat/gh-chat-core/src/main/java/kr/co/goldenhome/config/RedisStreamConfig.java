package kr.co.goldenhome.config;

import kr.co.goldenhome.messaging.ChatMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.util.Objects;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class RedisStreamConfig {

    private final StringRedisTemplate redisTemplate;
    private final ChatMessageListener chatMessageListener;
    private static final int consumerPoolSize = Runtime.getRuntime().availableProcessors();

    @Bean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer() {
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container =
                StreamMessageListenerContainer.create(Objects.requireNonNull(redisTemplate.getConnectionFactory()));


        String instanceId = UUID.randomUUID().toString();
        for (int i = 0; i < consumerPoolSize; i++) {
            String consumerName = "consumer-" + instanceId + "-" + i;
            // receive 메서드를 호출하여 각 컨슈머를 리스너 컨테이너에 등록합니다.
            container.receive(
                    Consumer.from("chat.group", consumerName),
                    // 이 StreamOffset은 컨테이너가 시작될 때 리스너를 등록하기 위한 플레이스홀더 역할입니다.
                    // onChatRoomCreated 이벤트 리스너가 실제로 동적 스트림을 구독합니다.
                    // ReadOffset.lastConsumed() 대신 ReadOffset.from("0-0") 또는 ReadOffset.latest() 등 사용 가능
                    StreamOffset.create("chat:room:1", ReadOffset.lastConsumed()),
                    chatMessageListener
            );
        }

        // 컨테이너가 시작될 때 리스너를 등록
        container.start();

        return container;
    }
}
