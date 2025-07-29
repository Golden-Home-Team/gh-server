package kr.co.goldenhome.controller;

import kr.co.goldenhome.dto.FacilityDetailResponse;
import kr.co.goldenhome.dto.FacilityResponse;
import kr.co.goldenhome.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    @GetMapping("/search")
    public List<FacilityResponse> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "facilityType", required = false) String facilityType,
            @RequestParam(value = "grade", required = false) String grade,
            @RequestParam(value = "sort", defaultValue = "relevance") String sort,
            @RequestParam(value = "withinYears", defaultValue = "0") int withinYears,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        return facilityService.search(name, address, facilityType, grade, sort, withinYears, page, size);
    }

    @GetMapping("/{facilityId}")
    public FacilityDetailResponse read(@PathVariable("facilityId") Long facilityId) {
        return facilityService.read(facilityId);
    }
}
