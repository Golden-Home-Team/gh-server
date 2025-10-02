package kr.co.goldenhome.messaging;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChatRoomCreatedEvent extends ApplicationEvent {

    private final Long chatRoomId;

    public ChatRoomCreatedEvent(Object source, Long chatRoomId) {
        super(source);
        this.chatRoomId = chatRoomId;
    }
}
