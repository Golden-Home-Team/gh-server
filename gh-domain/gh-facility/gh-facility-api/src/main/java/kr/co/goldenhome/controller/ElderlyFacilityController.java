package kr.co.goldenhome.controller;

import kr.co.goldenhome.dto.ElderlyFacilityResponse;
import kr.co.goldenhome.service.ElderlyFacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/facility")
@RequiredArgsConstructor
public class ElderlyFacilityController {

    private final ElderlyFacilityService elderlyFacilityService;

    @GetMapping("/{facilityId}")
    public ElderlyFacilityResponse read(@PathVariable("facilityId") Long facilityId) {
        return elderlyFacilityService.read(facilityId);
    }
}
