package kr.co.goldenhome.controller;

import jakarta.validation.Valid;
import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.dto.CommonResponse;
import kr.co.goldenhome.dto.TermsAgreementRequest;
import kr.co.goldenhome.dto.TermsRequest;
import kr.co.goldenhome.dto.TermsResponse;
import kr.co.goldenhome.entity.TermsType;
import kr.co.goldenhome.service.TermsService;
import kr.co.goldenhome.validator.EnumValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/terms")
public class TermsController {

    private final TermsService termsService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public CommonResponse create(@RequestBody @Valid TermsRequest request) {
        EnumValidator.validate(TermsType.class, "termsType", request.termsType(), "TermsController.create");
        termsService.create(request);
        return CommonResponse.ok();
    }

    @GetMapping
    public List<TermsResponse> getActiveTerms() {
        return termsService.getActiveTerms().stream().map(TermsResponse::from).toList();
    }

    @PostMapping("/agree")
    public CommonResponse agree(@RequestBody @Valid TermsAgreementRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        termsService.agree(request, userPrincipal.userId());
        return CommonResponse.ok();
    }
}
