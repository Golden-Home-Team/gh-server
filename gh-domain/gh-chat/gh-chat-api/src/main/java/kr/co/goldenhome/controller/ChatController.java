package kr.co.goldenhome.controller;

import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.constant.SessionConstant;
import kr.co.goldenhome.dto.ChatMessageRequest;
import kr.co.goldenhome.dto.ChatRoomResponse;
import kr.co.goldenhome.entity.ChatMessage;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.messaging.SessionAttributeAccessor;
import kr.co.goldenhome.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SessionAttributeAccessor sessionAttributeAccessor;

    @PostMapping
    public ChatRoomResponse enterRoom(@RequestParam(value = "facilityId") Long facilityId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long chatRoomId = chatService.enterRoom(facilityId, userPrincipal.userId());
        return new ChatRoomResponse(chatRoomId);
    }

    @MessageMapping("/message")
    public void sendMessage(ChatMessageRequest request, StompHeaderAccessor headerAccessor) {
        Long senderId = sessionAttributeAccessor.getById(headerAccessor, SessionConstant.USER_KEY);
        ChatMessage chatMessage = ChatMessage.create(request.chatRoomId(), senderId, request.content());
        chatService.chat(chatMessage);
    }
}
