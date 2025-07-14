package kr.co.goldenhome.controller;

import auth.UserPrincipal;
import dto.CommonResponse;
import kr.co.goldenhome.dto.ResumeCreateRequest;
import kr.co.goldenhome.dto.ResumeModifyRequest;
import kr.co.goldenhome.dto.ResumeResponse;
import kr.co.goldenhome.service.ResumeService;
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
        resumeService.create(request, userPrincipal.userId());
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
