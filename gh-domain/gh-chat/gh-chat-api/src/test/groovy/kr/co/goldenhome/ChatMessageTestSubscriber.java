package kr.co.goldenhome;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.goldenhome.entity.ChatMessage;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageTestSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate ;

    public ChatMessageTestSubscriber(ObjectMapper objectMapper, SimpMessageSendingOperations messagingTemplate) {
        this.objectMapper = objectMapper;
        this.messagingTemplate = messagingTemplate;
    }

    public void send(String publishedMessage) {
        try {
            ChatMessage chatMessage = objectMapper.readValue(publishedMessage, ChatMessage.class);
            messagingTemplate.convertAndSend("/topic/chat", chatMessage);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_PROCESSING_EXCEPTION, "ChatMessageSubscriber.send");
        } catch (Exception e) {
            throw e;
        }
    }
}
