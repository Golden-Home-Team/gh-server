package kr.co.goldenhome.controller;

import auth.UserPrincipal;
import dto.CommonResponse;
import kr.co.goldenhome.dto.ResumeSubmissionModifyRequest;
import kr.co.goldenhome.dto.ResumeSubmissionResponse;
import kr.co.goldenhome.service.ResumeSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes-submission")
public class ResumeSubmissionController {

    private final ResumeSubmissionService resumeSubmissionService;

    @PostMapping("/{facilityId}")
    public CommonResponse submission(@PathVariable("facilityId") Long facilityId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        resumeSubmissionService.create(facilityId, userPrincipal.userId());
        return CommonResponse.ok();
    }

    @GetMapping("/readAll")
    public List<ResumeSubmissionResponse> readAll(
            @RequestParam(value = "lastId", required = false) Long lastId,
            @RequestParam(value = "pageSize", defaultValue = "20") Long pageSize,
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ) {
        return resumeSubmissionService.readAll(userPrincipal.userId(), lastId, pageSize);
    }

    @GetMapping("/{id}")
    public ResumeSubmissionResponse read(@PathVariable("id") Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return resumeSubmissionService.read(id, userPrincipal.userId());
    }

    @PatchMapping("/{id}")
    public CommonResponse modify(@RequestBody ResumeSubmissionModifyRequest request, @PathVariable("id") Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        resumeSubmissionService.modify(request, id, userPrincipal.userId());
        return CommonResponse.ok();
    }


}
