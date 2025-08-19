package kr.co.goldenhome.controller;

import dto.CommonResponse;
import jakarta.validation.Valid;
import kr.co.goldenhome.dto.DailyShotMainResponse;
import kr.co.goldenhome.dto.DailyShotRequest;
import kr.co.goldenhome.dto.DailyShotResponse;
import kr.co.goldenhome.dto.DailyShotUpdateRequest;
import kr.co.goldenhome.service.DailyShotService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;

// todo API 문서
@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class DailyShotController {

    private final DailyShotService dailyShotService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isManager(#facilityId)")
    @PostMapping("/{facilityId}/daily-shot")
    public CommonResponse write(@Valid @RequestBody DailyShotRequest request, @PathVariable("facilityId") Long facilityId) {
        dailyShotService.write(request, facilityId);
        return CommonResponse.ok();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isManager(#facilityId)")
    @PutMapping("/{facilityId}/daily-shot/{dailyShotId}")
    public CommonResponse update(@Valid @RequestBody DailyShotUpdateRequest request, @PathVariable("dailyShotId") Long dailyShotId, @PathVariable("facilityId") Long facilityId) {
        dailyShotService.update(request, dailyShotId);
        return CommonResponse.ok();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isMember(#facilityId)")
    @GetMapping("/{facilityId}/daily-shot/main")
    public DailyShotMainResponse readOnMain(@PathVariable("facilityId") Long facilityId) {
        return dailyShotService.readOnMain(facilityId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isMember(#facilityId)")
    @GetMapping("/{facilityId}/daily-shot")
    public DailyShotResponse readByDayOfWeek(@PathVariable Long facilityId, @RequestParam(value = "dayOfWeek", defaultValue = "MONDAY") DayOfWeek dayOfWeek) {
        return dailyShotService.readByDayOfWeek(facilityId, dayOfWeek);
    }

}
