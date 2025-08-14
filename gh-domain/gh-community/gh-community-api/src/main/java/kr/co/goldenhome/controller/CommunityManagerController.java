package kr.co.goldenhome.controller;

import auth.UserPrincipal;
import kr.co.goldenhome.dto.InvitationLinkResponse;
import kr.co.goldenhome.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class CommunityManagerController {

    private final CommunityService communityService;
    @Value("${GOLDEN_HOME_SERVER_URL}")
    private String goldenHomeServerUrl;

    // todo API 문서
    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isMember(#facilityId)")
    @PostMapping("/{facilityId}/admin")
    public InvitationLinkResponse generateInvitation(@PathVariable("facilityId") Long facilityId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String code = communityService.generateInvitationCode(facilityId, userPrincipal.userId());
        return new InvitationLinkResponse(code, goldenHomeServerUrl + "/api/communities/enter?code=" + code);
    }
}
