package kr.co.goldenhome;

import kr.co.goldenhome.messaging.ChatMessageSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class RedisConfig {

    private final ChatMessageSubscriber chatMessageTestSubscriber;

    public RedisConfig(ChatMessageSubscriber chatMessageTestSubscriber) {
        this.chatMessageTestSubscriber = chatMessageTestSubscriber;
    }

    @Bean
    public ThreadPoolTaskExecutor redisStreamExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("redis-stream-");
        executor.initialize();
        return executor;
    }

    @Bean
    public ChannelTopic chatMessageTopic() {
        return new ChannelTopic("chatMessageTopic");
    }

    @Bean
    public MessageListenerAdapter chatMessageListenerAdapter() {
        return new MessageListenerAdapter(chatMessageTestSubscriber, "send");
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter chatMessageListenerAdapter,
            ChannelTopic chatMessageTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(chatMessageListenerAdapter, chatMessageTopic);
        return container;
    }

    @Bean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                .executor(redisStreamExecutor())
                .batchSize(10)
                .pollTimeout(Duration.ofSeconds(10))
                .build();
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(redisConnectionFactory, options);
        container.start();
        return container;
    }
}
