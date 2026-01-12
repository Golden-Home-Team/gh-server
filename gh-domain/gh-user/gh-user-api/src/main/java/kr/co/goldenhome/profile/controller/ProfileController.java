package kr.co.goldenhome.profile.controller;

import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.dto.CommonResponse;
import jakarta.validation.Valid;
import kr.co.goldenhome.profile.dto.*;
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
    public CommonResponse createProfileImage(@Valid @RequestBody ProfileImageRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        profileService.createProfileImage(request, userPrincipal.userId());
        return CommonResponse.ok();
    }

    @PutMapping("/name")
    public CommonResponse modifyName(@Valid @RequestBody ProfileNameRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        profileService.modifyName(request, userPrincipal.userId());
        return CommonResponse.ok();
    }

    @PutMapping("/loginId")
    public CommonResponse modifyLoginId(@Valid @RequestBody ProfileLoginIdRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        profileService.modifyLoginId(request, userPrincipal.userId());
        return CommonResponse.ok();
    }

}
