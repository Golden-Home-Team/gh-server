package kr.co.goldenhome.controller;

import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.dto.ChatMessageRequest;
import kr.co.goldenhome.dto.ChatRoomResponse;
import kr.co.goldenhome.entity.ChatMessage;
import kr.co.goldenhome.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
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

    @PostMapping
    public ChatRoomResponse enterRoom(@RequestParam(value = "facilityId") Long facilityId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long chatRoomId = chatService.enterRoom(facilityId, userPrincipal.userId());
        return new ChatRoomResponse(chatRoomId);
    }

    @MessageMapping("/message")
    public void sendMessage(ChatMessageRequest request, Principal principal) {
        Long senderId = Long.parseLong(principal.getName());
        ChatMessage chatMessage = ChatMessage.create(request.chatRoomId(), senderId, request.content());
        chatService.chat(chatMessage);
    }
}
