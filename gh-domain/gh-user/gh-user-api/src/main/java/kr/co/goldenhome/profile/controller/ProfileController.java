package kr.co.goldenhome.profile.controller;

import auth.UserPrincipal;
import dto.CommonResponse;
import kr.co.goldenhome.profile.dto.ProfileImageRequest;
import kr.co.goldenhome.profile.dto.ProfileResponse;
import kr.co.goldenhome.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ProfileResponse get(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return profileService.get(userPrincipal.userId());
    }

    @PostMapping
    public CommonResponse createProfileImage(@RequestBody ProfileImageRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        profileService.createProfileImage(request, userPrincipal.userId());
        return CommonResponse.ok();
    }
}
