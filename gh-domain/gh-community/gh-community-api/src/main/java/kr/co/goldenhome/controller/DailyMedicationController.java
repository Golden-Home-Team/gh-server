package kr.co.goldenhome.controller;

import kr.co.goldenhome.dto.CommonResponse;
import jakarta.validation.Valid;
import kr.co.goldenhome.dto.DailyMedicationRequest;
import kr.co.goldenhome.dto.DailyMedicationResponse;
import kr.co.goldenhome.dto.DailyMedicationUpdateRequest;
import kr.co.goldenhome.service.DailyMedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class DailyMedicationController {

    private final DailyMedicationService dailyMedicationService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isManager(#facilityId)")
    @PostMapping("/{facilityId}/daily-medication")
    public CommonResponse write(@Valid @RequestBody DailyMedicationRequest request, @PathVariable("facilityId") Long facilityId) {
        dailyMedicationService.write(request, facilityId);
        return CommonResponse.ok();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isManager(#facilityId)")
    @PutMapping("/{facilityId}/daily-medication/{dailyMedicationId}")
    public CommonResponse update(@RequestBody DailyMedicationUpdateRequest request, @PathVariable("dailyMedicationId") Long dailyMedicationId, @PathVariable("facilityId") Long facilityId) {
        dailyMedicationService.update(request, dailyMedicationId);
        return CommonResponse.ok();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isMember(#facilityId)")
    @GetMapping("/{facilityId}/daily-medication")
    public DailyMedicationResponse readByDayOfWeek(@PathVariable Long facilityId, @RequestParam(value = "dayOfWeek", defaultValue = "MONDAY") DayOfWeek dayOfWeek) {
        return dailyMedicationService.readByDayOfWeek(facilityId, dayOfWeek);
    }


}
