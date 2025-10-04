package kr.co.goldenhome.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    public void send(String publishedMessage) {
        try {
//            objectMapper.readValue()
        } catch (Exception e) {

        }
//        messagingTemplate.convertAndSend("/topic/chat", message);
    }
}
