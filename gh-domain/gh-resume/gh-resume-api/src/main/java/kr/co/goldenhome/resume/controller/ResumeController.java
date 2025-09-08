package kr.co.goldenhome.resume.controller;

import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.dto.CommonResponse;
import kr.co.goldenhome.enums.*;
import kr.co.goldenhome.resume.dto.ResumeCreateRequest;
import kr.co.goldenhome.resume.dto.ResumeModifyRequest;
import kr.co.goldenhome.resume.dto.ResumeResponse;
import kr.co.goldenhome.resume.service.ResumeService;
import kr.co.goldenhome.validator.EnumValidator;
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
        enumValidate(request.gender(), request.physicalCondition(), request.longTermCareGrade(), request.healthInsurance(), request.relationship(), request.admissionTimeFrame());
        resumeService.write(request, userPrincipal.userId());
        return CommonResponse.ok();
    }

    @GetMapping
    public ResumeResponse read(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return resumeService.read(userPrincipal.userId());
    }

    @PutMapping
    public CommonResponse modify(@RequestBody ResumeModifyRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        enumValidate(request.gender(), request.physicalCondition(), request.longTermCareGrade(), request.healthInsurance(), request.relationship(), request.admissionTimeFrame());
        resumeService.modify(request, userPrincipal.userId());
        return CommonResponse.ok();
    }

    private void enumValidate(String gender, String s, String s2, String s3, String relationship, String s4) {
        EnumValidator.validate(Gender.class, "gender", gender, "ResumeController.write");
        EnumValidator.validate(PhysicalCondition.class, "physicalCondition", s, "ResumeController.write");
        EnumValidator.validate(LongTermCareGrade.class, "longTermCareGrade", s2, "ResumeController.write");
        EnumValidator.validate(HealthInsurance.class, "healthInsurance", s3, "ResumeController.write");
        EnumValidator.validate(Relationship.class, "relationship", relationship, "ResumeController.write");
        EnumValidator.validate(AdmissionTimeFrame.class, "admissionTimeFrame", s4, "ResumeController.write");
    }
}
