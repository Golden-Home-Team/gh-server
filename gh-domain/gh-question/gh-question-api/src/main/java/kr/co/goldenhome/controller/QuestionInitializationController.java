package kr.co.goldenhome.controller;

import kr.co.goldenhome.dto.*;
import kr.co.goldenhome.service.QuestionInitializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionInitializationController {

    private final QuestionInitializationService questionInitializationService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/domain")
    public CommonResponse createQuestionDomain(@RequestBody QuestionDomainRequest request) {
        questionInitializationService.createQuestionDomain(request);
        return CommonResponse.ok();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/option")
    public CommonResponse createQuestionDomainOption(@RequestBody QuestionDomainOptionRequest request) {
        questionInitializationService.createQuestionDomainOption(request);
        return CommonResponse.ok();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/score")
    public CommonResponse createScoreConversion(@RequestBody ScoreConversionRequest request) {
        questionInitializationService.createScoreConversion(request);
        return CommonResponse.ok();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/scores")
    public CommonResponse createScoreConversions(@RequestBody List<ScoreConversionRequest> requests) {
        questionInitializationService.createScoreConversions(requests);
        return CommonResponse.ok();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    public CommonResponse create(@RequestBody List<QuestionRequest> requests) {
        questionInitializationService.create(requests);
        return CommonResponse.ok();
    }

}
