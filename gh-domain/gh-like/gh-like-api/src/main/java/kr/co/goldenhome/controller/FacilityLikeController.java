package kr.co.goldenhome.controller;

import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.dto.CommonResponse;
import kr.co.goldenhome.service.FacilityLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes/facility")
public class FacilityLikeController {

    private final FacilityLikeService facilityLikeService;

    @PostMapping("/{facilityId}")
    public CommonResponse like(
            @PathVariable("facilityId") Long facilityId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        facilityLikeService.like(facilityId, userPrincipal.userId());
        return CommonResponse.ok();
    }

    @DeleteMapping("/{facilityId}")
    public CommonResponse unlike(
            @PathVariable("facilityId") Long facilityId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        facilityLikeService.unlike(facilityId, userPrincipal.userId());
        return CommonResponse.ok();
    }
}
