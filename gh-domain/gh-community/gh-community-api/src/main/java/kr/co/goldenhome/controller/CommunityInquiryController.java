package kr.co.goldenhome.controller;

import auth.UserPrincipal;
import dto.CommonResponse;
import jakarta.validation.Valid;
import kr.co.goldenhome.dto.CommunityInquiryRequest;
import kr.co.goldenhome.dto.CommunityInquiryResponse;
import kr.co.goldenhome.entity.CommunityInquiry;
import kr.co.goldenhome.enums.CommunityInquiryType;
import kr.co.goldenhome.service.CommunityInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import validator.EnumValidator;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class CommunityInquiryController {

    private final CommunityInquiryService communityInquiryService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isMember(#facilityId)")
    @PostMapping("/{facilityId}/inquiry")
    public CommonResponse write(
            @Valid @RequestBody CommunityInquiryRequest request,
            @PathVariable("facilityId") Long facilityId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        EnumValidator.validate(CommunityInquiryType.class, "type", request.type(), "CommunityInquiryController.write");
        communityInquiryService.write(request, facilityId, userPrincipal.userId());
        return CommonResponse.ok();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isMember(#facilityId)")
    @GetMapping("/{facilityId}/inquiry/{inquiryId}")
    public CommunityInquiryResponse read(@PathVariable("inquiryId") Long inquiryId, @PathVariable("facilityId") Long facilityId) {
        CommunityInquiry communityInquiry = communityInquiryService.read(inquiryId);
        return CommunityInquiryResponse.from(communityInquiry);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @communitySecurityManager.isMember(#facilityId)")
    @GetMapping("/{facilityId}/inquiry")
    public List<CommunityInquiryResponse> readAll(
            @PathVariable("facilityId") Long facilityId,
            @RequestParam(value = "lastId", required = false) Long lastId,
            @RequestParam(value = "pageSize", defaultValue = "20") Long pageSize) {
        return communityInquiryService.readAll(facilityId, lastId, pageSize)
                .stream().map(CommunityInquiryResponse::convertContent).collect(Collectors.toList());
    }
}
