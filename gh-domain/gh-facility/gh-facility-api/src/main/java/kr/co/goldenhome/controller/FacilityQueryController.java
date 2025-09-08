    package kr.co.goldenhome.controller;

import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.dto.FacilityDetailResponse;
import kr.co.goldenhome.dto.FacilityResponse;
import kr.co.goldenhome.service.FacilityQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
public class FacilityQueryController {

    private final FacilityQueryService facilityQueryService;

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
        return facilityQueryService.search(name, address, facilityType, grade, sort, withinYears, page, size);
    }

    @GetMapping("/{facilityId}")
    public FacilityDetailResponse read(@PathVariable("facilityId") Long facilityId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = (userPrincipal != null) ? userPrincipal.userId() : null;
        return facilityQueryService.read(facilityId, userId);
    }

    @GetMapping("/like")
    public List<FacilityResponse> getLikedFacilities(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return facilityQueryService.getLikedFacilities(userPrincipal.userId());
    }
}
