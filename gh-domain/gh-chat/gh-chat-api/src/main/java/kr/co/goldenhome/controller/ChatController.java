package kr.co.goldenhome.controller;

import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.dto.ChatRoomResponse;
import kr.co.goldenhome.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/room")
    public ChatRoomResponse enterRoom(@RequestParam Long facilityId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long chatRoomId = chatService.enterRoom(facilityId, userPrincipal.userId());
        return new ChatRoomResponse(chatRoomId);
    }
}
