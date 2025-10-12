package kr.co.goldenhome.messaging;

import kr.co.goldenhome.repository.ChatConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.time.Duration;

import static kr.co.goldenhome.constant.SessionConstant.USER_KEY;

@Component
@RequiredArgsConstructor
public class StompEventListener {

    private final SessionAttributeAccessor sessionAttributeAccessor;
    private final ChatConnectionRepository chatConnectionRepository;

    @EventListener
    public void subscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String destination = accessor.getDestination();
        Long userId = sessionAttributeAccessor.getById(accessor, USER_KEY);
        System.out.println("sessionId: " + sessionId + ", userId: " + userId + ", destination: " + destination);
        if (destination != null && destination.startsWith("/topic/chat/")) {
            long chatRoomId = Long.parseLong(destination.substring("/topic/chat/".length()));
            System.out.println("chatRoomId = " + chatRoomId);
            chatConnectionRepository.connect(userId, sessionId, chatRoomId, Duration.ofMinutes(30));
        }
    }

    @EventListener
    public void disconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        Long userId = sessionAttributeAccessor.getById(accessor, USER_KEY);
        chatConnectionRepository.disconnect(userId, sessionId);
    }
}
