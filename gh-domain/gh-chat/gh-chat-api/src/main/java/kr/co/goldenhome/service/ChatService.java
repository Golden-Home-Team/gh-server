package kr.co.goldenhome.service;

import kr.co.goldenhome.CommunityApi;
import kr.co.goldenhome.implement.ChatManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final CommunityApi communityApi;
    private final ChatManager chatManager;

    public Long enterRoom(Long facilityId, Long userId) {
        Long communityManagerUserId = communityApi.getCommunityManagerUserId(facilityId);
        return chatManager.getOrCreate(facilityId, userId, communityManagerUserId);
    }
}
