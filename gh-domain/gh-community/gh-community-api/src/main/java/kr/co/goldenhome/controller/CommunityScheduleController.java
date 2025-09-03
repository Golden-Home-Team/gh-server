package kr.co.goldenhome.controller;

import kr.co.goldenhome.dto.CommonResponse;
import jakarta.validation.Valid;
import kr.co.goldenhome.dto.CommunityScheduleRequest;
import kr.co.goldenhome.dto.CommunityScheduleResponse;
import kr.co.goldenhome.dto.CommunityScheduleUpdateRequest;
import kr.co.goldenhome.service.CommunityScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Month;
import java.util.List;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class CommunityScheduleController {

    private final CommunityScheduleService communityScheduleService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isManager(#facilityId)")
    @PostMapping("/{facilityId}/schedule")
    public CommonResponse write(@Valid @RequestBody CommunityScheduleRequest request, @PathVariable("facilityId") Long facilityId) {
        communityScheduleService.write(request, facilityId);
        return CommonResponse.ok();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isManager(#facilityId)")
    @PutMapping("/{facilityId}/schedule/{scheduleId}")
    public CommonResponse update(@Valid @RequestBody CommunityScheduleUpdateRequest request, @PathVariable("scheduleId") Long scheduleId, @PathVariable("facilityId") Long facilityId) {
        communityScheduleService.update(request, scheduleId);
        return CommonResponse.ok();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isMember(#facilityId)")
    @GetMapping("/{facilityId}/schedule")
    public List<CommunityScheduleResponse> readByMonth(@PathVariable("facilityId") Long facilityId, @RequestParam(value = "month") Month month) {
        return communityScheduleService.readByMonth(facilityId, month);
    }


}
