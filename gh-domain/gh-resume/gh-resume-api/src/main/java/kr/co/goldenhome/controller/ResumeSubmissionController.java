package kr.co.goldenhome.controller;

import auth.UserPrincipal;
import dto.CommonResponse;
import kr.co.goldenhome.service.ResumeSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes-submission")
public class ResumeSubmissionController {

    private final ResumeSubmissionService resumeSubmissionService;

    @PostMapping("/{facilityId}")
    public CommonResponse submission(@PathVariable("facilityId") Long facilityId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return CommonResponse.ok();
    }
}
