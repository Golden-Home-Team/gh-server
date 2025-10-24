package kr.co.goldenhome.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.goldenhome.FcmSender;
import kr.co.goldenhome.NotificationsRequest;
import kr.co.goldenhome.UserApi;
import kr.co.goldenhome.entity.ChatMessage;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.repository.ChatConnectionRepository;
import kr.co.goldenhome.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatUserRepository chatUserRepository;
    private final ChatConnectionRepository chatConnectionRepository;
    private final UserApi userApi;
    private final FcmSender fcmSender;

    public void send(String publishedMessage) {
        try {
            ChatMessage chatMessage = objectMapper.readValue(publishedMessage, ChatMessage.class);
            Long chatRoomId = chatMessage.getChatRoomId();
            Long senderId = chatMessage.getSenderId();
            messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, chatMessage);
            List<Long> list = chatUserRepository.findByChatRoomId(chatRoomId).stream()
                    .map(chatUser -> {
                        Long userId = chatUser.getUserId();
                        if (!chatConnectionRepository.isUserViewingChatRoom(userId, chatRoomId)) return userId;
                        return null;
                    }).toList();
            List<String> fcmTokens = userApi.getFcmTokens(list);
            String userName = userApi.getUserName(senderId);
            fcmSender.sendMessages(new NotificationsRequest(
                    fcmTokens,
                    userName,
                    publishedMessage
            ));

        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_PROCESSING_EXCEPTION, "ChatMessageSubscriber.send");
        } catch (Exception e) {
            log.error("ChatMessageSubscriber.send", e);
            throw e;
        }
    }
}
