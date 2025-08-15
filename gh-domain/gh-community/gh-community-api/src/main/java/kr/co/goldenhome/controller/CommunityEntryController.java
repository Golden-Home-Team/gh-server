package kr.co.goldenhome.controller;

import auth.UserPrincipal;
import dto.CommonResponse;


import kr.co.goldenhome.dto.InvitationLinkResponse;
import kr.co.goldenhome.service.CommunityEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

// todo API 문서
@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class CommunityEntryController {

    private final CommunityEntryService communityEntryService;
    @Value("${GOLDEN_HOME_FRONT_URL}")
    private String goldenHomeFrontUrl;
    @Value("${GOLDEN_HOME_SERVER_URL}")
    private String goldenHomeServerUrl;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isCommunityAdmin(#facilityId)")
    @PostMapping("/{facilityId}/admin")
    public InvitationLinkResponse generateInvitation(@PathVariable("facilityId") Long facilityId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String code = communityEntryService.generateInvitationCode(facilityId, userPrincipal.userId());
        return new InvitationLinkResponse(code, goldenHomeServerUrl + "/api/communities/enter?code=" + code);
    }

    @GetMapping("/enter")
    public ResponseEntity<Void> redirectEnter(@RequestParam("code") String code)  {
        String url = UriComponentsBuilder.fromUriString(goldenHomeFrontUrl + "/front-api")
                .queryParam("code", code)
                .build()
                .toUriString();
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, url).build();
    }

    @PostMapping("/enter")
    public CommonResponse enter(@RequestParam("code") String code, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        communityEntryService.enter(code, userPrincipal.userId());
        return CommonResponse.ok();
    }
}
