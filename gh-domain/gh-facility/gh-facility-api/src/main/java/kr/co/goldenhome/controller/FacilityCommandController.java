package kr.co.goldenhome.controller;

import kr.co.goldenhome.dto.CommonResponse;
import jakarta.validation.Valid;
import kr.co.goldenhome.dto.FacilityProfileRequest;
import kr.co.goldenhome.service.FacilityCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
public class FacilityCommandController {

    private final FacilityCommandService facilityCommandService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isManager(#facilityId)")
    @PostMapping("/{facilityId}/profile")
    public CommonResponse uploadProfile(@Valid @RequestBody FacilityProfileRequest request, @PathVariable("facilityId") Long facilityId) {
        facilityCommandService.uploadProfile(facilityId, request.formattedImageName());
        return CommonResponse.ok();
    }
}
