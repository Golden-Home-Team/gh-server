package kr.co.goldenhome.controller;

import kr.co.goldenhome.dto.ElderlyFacilityResponse;
import kr.co.goldenhome.service.ElderlyFacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facility")
@RequiredArgsConstructor
public class ElderlyFacilityController {

    private final ElderlyFacilityService elderlyFacilityService;

    @GetMapping("/{facilityId}")
    public ElderlyFacilityResponse read(@PathVariable("facilityId") Long facilityId) {
        return elderlyFacilityService.read(facilityId);
    }

    @GetMapping("/v1/readAll")
    public List<ElderlyFacilityResponse> readAll(
            @RequestParam("facilityType") String facilityType,
            @RequestParam(value = "lastId", required = false) Long lastId,
            @RequestParam(value = "pageSize", defaultValue = "20") Long pageSize
    ) {
        return elderlyFacilityService.readAll(facilityType, lastId, pageSize);
    }
}
