package kr.co.goldenhome.controller;

import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.dto.CommonResponse;
import kr.co.goldenhome.dto.CommunityCombinedResponse;
import kr.co.goldenhome.dto.MyCommunityResponse;
import kr.co.goldenhome.service.CommunityQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class CommunityQueryController {

    private final CommunityQueryService communityQueryService;

    @GetMapping("/{facilityId}/check")
    public CommonResponse isCommunityUser(@PathVariable("facilityId") Long facilityId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new CommonResponse(communityQueryService.isCommunityUser(facilityId, userPrincipal.userId()));
    }

    @GetMapping("/{facilityId}")
    public CommunityCombinedResponse read(@PathVariable("facilityId") Long facilityId) {
        return communityQueryService.read(facilityId);
    }

    @GetMapping("/me")
    public List<MyCommunityResponse> myJoinedCommunity(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return communityQueryService.myJoinedCommunity(userPrincipal.userId());
    }

}
