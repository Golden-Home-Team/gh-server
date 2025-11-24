package kr.co.goldenhome.messaging;

import kr.co.goldenhome.FcmManager;
import kr.co.goldenhome.NotificationsRequest;
import kr.co.goldenhome.UserApi;
import kr.co.goldenhome.repository.ChatConnectionRepository;
import kr.co.goldenhome.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatNotificationEventListener {

    private final ChatUserRepository chatUserRepository;
    private final ChatConnectionRepository chatConnectionRepository;
    private final UserApi userApi;
    private final FcmManager fcmManager;

    @Async
    @EventListener
    public void handleChatNotification(ChatNotificationEvent event) {
            List<Long> list = chatUserRepository.findByChatRoomId(event.chatRoomId()).stream()
                    .map(chatUser -> {
                        Long userId = chatUser.getUserId();
                        if (!chatConnectionRepository.isUserViewingChatRoom(userId, event.chatRoomId())) return userId;
                        return null;
                    }).toList();
            List<String> fcmTokens = userApi.getFcmTokens(list, "CHAT");
            String userName = userApi.getUserName(event.senderId());
            fcmManager.sendMessages(new NotificationsRequest(
                    fcmTokens,
                    userName,
                    event.content()
            ));
    }
}
