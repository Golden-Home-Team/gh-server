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

    @GetMapping("/v2/readAll")
    public List<ElderlyFacilityResponse> search(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "facilityType", required = false) String facilityType,
            @RequestParam(value = "grade", required = false) String grade,
            @RequestParam(value = "minPrice", defaultValue = "0") double minPrice,
            @RequestParam(value = "maxPrice", defaultValue = "100000000") double maxPrice,
            @RequestParam(value = "withinYears", defaultValue = "0") int withinYears,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        return elderlyFacilityService.search(query, address, facilityType, grade, minPrice, maxPrice, withinYears, page, size);
    }
}
