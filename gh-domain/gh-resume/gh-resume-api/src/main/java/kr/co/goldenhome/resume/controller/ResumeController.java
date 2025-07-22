package kr.co.goldenhome.resume.controller;

import auth.UserPrincipal;
import dto.CommonResponse;
import kr.co.goldenhome.resume.dto.ResumeCreateRequest;
import kr.co.goldenhome.resume.dto.ResumeModifyRequest;
import kr.co.goldenhome.resume.dto.ResumeResponse;
import kr.co.goldenhome.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public CommonResponse write(@RequestBody ResumeCreateRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        resumeService.write(request, userPrincipal.userId());
        return CommonResponse.ok();
    }

    @GetMapping
    public ResumeResponse read(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return resumeService.read(userPrincipal.userId());
    }

    @PatchMapping
    public CommonResponse modify(@RequestBody ResumeModifyRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        resumeService.modify(request, userPrincipal.userId());
        return CommonResponse.ok();
    }
}
