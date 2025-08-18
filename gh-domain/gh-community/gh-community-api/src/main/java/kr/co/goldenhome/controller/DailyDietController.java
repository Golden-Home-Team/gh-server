package kr.co.goldenhome.controller;

import dto.CommonResponse;
import jakarta.validation.Valid;
import kr.co.goldenhome.dto.DailyDietRequest;
import kr.co.goldenhome.dto.DailyDietResponse;
import kr.co.goldenhome.dto.DailyDietThumbnailResponse;
import kr.co.goldenhome.dto.DailyDietUpdateRequest;
import kr.co.goldenhome.service.DailyDietService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// todo API 문서
@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class DailyDietController {

    private final DailyDietService dailyDietService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isManager(#facilityId)")
    @PostMapping("/{facilityId}/daily-diet")
    public CommonResponse write(@Valid @RequestBody DailyDietRequest request, @PathVariable("facilityId") Long facilityId) {
        dailyDietService.write(request, facilityId);
        return CommonResponse.ok();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isManager(#facilityId)")
    @PutMapping("/{facilityId}/daily-diet/{dailyDietId}")
    public CommonResponse update(@RequestBody DailyDietUpdateRequest request, @PathVariable("dailyDietId") Long dailyDietId, @PathVariable Long facilityId) {
        dailyDietService.update(request, dailyDietId);
        return CommonResponse.ok();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isMember(#facilityId)")
    @GetMapping("/{facilityId}/daily-diet/main")
    public DailyDietThumbnailResponse readOnMain(@PathVariable("facilityId") Long facilityId) {
        return dailyDietService.readOnMain(facilityId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isMember(#facilityId)")
    @GetMapping("/{facilityId}/daily-diet/{dailyDietId}")
    public DailyDietResponse read(@PathVariable("dailyDietId") Long dailyDietId, @PathVariable Long facilityId) {
        return dailyDietService.read(dailyDietId);
    }


}
