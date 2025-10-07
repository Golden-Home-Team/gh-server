package kr.co.goldenhome.messaging;

import kr.co.goldenhome.entity.ChatMessage;
import kr.co.goldenhome.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import static kr.co.goldenhome.constant.RedisConstants.CHAT_CONSUMER_GROUP;
import static kr.co.goldenhome.constant.RedisConstants.CHAT_MESSAGE_STREAM_KEY;

@Component
@RequiredArgsConstructor
public class ChatMessageListener implements StreamListener<String, MapRecord<String, String, String>> {

    private final ChatMessageRepository chatMessageRepository;
    private final StringRedisTemplate redisTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> record) {
        ChatMessage chatMessage = ChatMessage.convert(record.getValue());
        chatMessageRepository.save(chatMessage);
        redisTemplate.opsForStream().acknowledge(CHAT_MESSAGE_STREAM_KEY, CHAT_CONSUMER_GROUP, record.getId());
    }

}
