package kr.co.goldenhome.controller;

import kr.co.goldenhome.dto.CommonResponse;
import jakarta.validation.Valid;
import kr.co.goldenhome.dto.DailyRehabilitationRequest;
import kr.co.goldenhome.dto.DailyRehabilitationResponse;
import kr.co.goldenhome.dto.DailyRehabilitationUpdateRequest;
import kr.co.goldenhome.service.DailyRehabilitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class DailyRehabilitationController {

    private final DailyRehabilitationService dailyRehabilitationService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isManager(#facilityId)")
    @PostMapping("/{facilityId}/daily-rehab")
    public CommonResponse write(@Valid @RequestBody DailyRehabilitationRequest request, @PathVariable("facilityId") Long facilityId) {
        dailyRehabilitationService.write(request, facilityId);
        return CommonResponse.ok();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isManager(#facilityId)")
    @PutMapping("/{facilityId}/daily-rehab/{dailyRehabId}")
    public CommonResponse update(@Valid @RequestBody DailyRehabilitationUpdateRequest request, @PathVariable("dailyRehabId") Long dailyRehabId, @PathVariable("facilityId") Long facilityId) {
        dailyRehabilitationService.update(request, dailyRehabId);
        return CommonResponse.ok();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isMember(#facilityId)")
    @GetMapping("/{facilityId}/daily-rehab")
    public DailyRehabilitationResponse readByDayOfWeek(@PathVariable Long facilityId, @RequestParam(value = "dayOfWeek", defaultValue = "MONDAY") DayOfWeek dayOfWeek) {
        return dailyRehabilitationService.readByDayOfWeek(facilityId, dayOfWeek);
    }

}
