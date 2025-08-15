package kr.co.goldenhome.controller;

import auth.UserPrincipal;
import dto.CommonResponse;
import jakarta.validation.Valid;
import kr.co.goldenhome.dto.CommunityNoticeRequest;
import kr.co.goldenhome.dto.CommunityNoticeResponse;
import kr.co.goldenhome.entity.CommunityNotice;
import kr.co.goldenhome.service.CommunityNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// todo api 문서
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
public class CommunityNoticeController {

    private final CommunityNoticeService communityNoticeService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isCommunityAdmin(#facilityId)")
    @PostMapping("/{facilityId}/notice")
    public CommonResponse create(@Valid @RequestBody CommunityNoticeRequest request, @PathVariable("facilityId") Long facilityId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        communityNoticeService.create(request, facilityId, userPrincipal.userId());
        return CommonResponse.ok();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isMember(#facilityId)")
    @GetMapping("/{facilityId}/notice/{noticeId}")
    public CommunityNoticeResponse read(@PathVariable("noticeId") Long noticeId) {
        CommunityNotice communityNotice = communityNoticeService.read(noticeId);
        return CommunityNoticeResponse.create(communityNotice);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isMember(#facilityId)")
    @GetMapping("/{facilityId}/notice")
    public List<CommunityNoticeResponse> readAll(
            @PathVariable("facilityId") Long facilityId,
            @RequestParam(value = "lastId", required = false) Long lastId,
            @RequestParam(value = "pageSize", defaultValue = "20") Long pageSize
    ) {
        return communityNoticeService.readAll(facilityId, lastId, pageSize)
                .stream()
                .map(CommunityNoticeResponse::create)
                .toList();
    }
}
