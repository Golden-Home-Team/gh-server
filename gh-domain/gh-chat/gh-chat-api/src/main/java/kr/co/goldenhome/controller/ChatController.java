package kr.co.goldenhome.controller;

import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.constant.SessionConstant;
import kr.co.goldenhome.dto.*;
import kr.co.goldenhome.entity.ChatMessage;
import kr.co.goldenhome.messaging.SessionAttributeAccessor;
import kr.co.goldenhome.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SessionAttributeAccessor sessionAttributeAccessor;

    @PostMapping("/{facilityId}")
    public ChatRoomResponse enterRoomWithCommunityManager(@PathVariable(value = "facilityId") Long facilityId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long chatRoomId = chatService.enterRoom(facilityId, userPrincipal.userId());
        return new ChatRoomResponse(chatRoomId);
    }

    @GetMapping
    public SliceResponse<ChatRoomMetadataRepositoryResponse> getChatRooms(
            @RequestParam(value = "cursor", required = false) LocalDateTime cursor,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return chatService.getChatRooms(cursor,userPrincipal.userId(), pageSize);
    }

    @GetMapping("/{chatRoomId}")
    public SliceResponse<ChatMessageResponse> getChatMessages(
            @PathVariable("chatRoomId") Long chatRoomId,
            @RequestParam(value = "cursor", required = false) LocalDateTime cursor,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return chatService.getChatMessages(chatRoomId, cursor,userPrincipal.userId(), pageSize);
    }

    @MessageMapping("/message")
    public void sendMessage(ChatMessageRequest request, StompHeaderAccessor headerAccessor) {
        Long senderId = sessionAttributeAccessor.getById(headerAccessor, SessionConstant.USER_KEY);
        ChatMessage chatMessage = ChatMessage.create(request.chatRoomId(), senderId, request.content());
        chatService.chat(chatMessage);
    }
}
