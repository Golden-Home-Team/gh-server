package kr.co.goldenhome.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatRoomEventListener {

    private final StreamMessageListenerContainer<String, MapRecord<String, String, String>> container;
    private final StringRedisTemplate redisTemplate;
    private final ChatMessageListener chatMessageListener;
    private static final String CHAT_GROUP_NAME = "chat.group";

    @EventListener
    public void onChatRoomCreated(ChatRoomCreatedEvent event) {
        Long chatRoomId = event.getChatRoomId();
        String streamKey = "chat:room:" + chatRoomId;
        String consumerName = "consumer-" + UUID.randomUUID();
        try {
            // 1. 새로운 스트림에 컨슈머 그룹 생성
            // '0-0'은 스트림의 가장 처음부터 메시지를 읽도록 설정
            redisTemplate.opsForStream().createGroup(streamKey, ReadOffset.from("0-0"), CHAT_GROUP_NAME);
            System.out.println("✅ 새로운 스트림 '" + streamKey + "'에 컨슈머 그룹 '" + CHAT_GROUP_NAME + "' 생성 완료.");
        } catch (RedisSystemException e) {
            // 이미 그룹이 존재하면 경고를 출력하고 넘어감
            if (Objects.requireNonNull(e.getCause()).getMessage().contains("BUSYGROUP")) {
                System.out.println("⚠️ 컨슈머 그룹 '" + CHAT_GROUP_NAME + "'는 이미 '" + streamKey + "'에 존재합니다.");
            } else {
                throw e;
            }
        }

        // 2. 리스너 컨테이너에 새로운 스트림 구독 등록
        // 이 리스너는 해당 스트림에서 'chat.group'에 속한 'consumerName'이 읽지 않은 메시지를 가져와 처리합니다.
        // ** 이러면 채팅방마다 컨슈머가 생성된다. 어느시점부터 컨슈머가 필요이상 많아지면 비효율적으로 될 것. Config에서 고정된 컨슈머를 사용하도록 설정해야함
//        container.receive(
//                Consumer.from(CHAT_GROUP_NAME, consumerName),
//                StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
//                chatMessageListener
//        );

    }
}
