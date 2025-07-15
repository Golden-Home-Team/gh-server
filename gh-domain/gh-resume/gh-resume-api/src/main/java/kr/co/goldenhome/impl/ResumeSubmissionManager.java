package kr.co.goldenhome.impl;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.ElderlyFacilityApi;
import kr.co.goldenhome.dto.ResumeSubmissionModifyRequest;
import kr.co.goldenhome.dto.ResumeSubmissionResponse;
import kr.co.goldenhome.entity.Resume;
import kr.co.goldenhome.entity.ResumeSubmission;
import kr.co.goldenhome.repository.ResumeRepository;
import kr.co.goldenhome.repository.ResumeSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResumeSubmissionManager {

    private final ResumeSubmissionRepository resumeSubmissionRepository;
    private final ResumeRepository resumeRepository;
    private final ElderlyFacilityApi elderlyFacilityApi;

    public void create(Long facilityId, Long userId) {
        Resume resume = resumeRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESUME, "ResumeSubmissionService.create"));
        if (!elderlyFacilityApi.existsById(facilityId)) throw new CustomException(ErrorCode.NOT_FOUND_FACILITY, "ResumeSubmissionService.create");
        resumeSubmissionRepository.save(ResumeSubmission.create(resume, facilityId));
    }

    public ResumeSubmissionResponse read(Long resumeSubmissionId, Long userId) {
        ResumeSubmission resumeSubmission = resumeSubmissionRepository.findById(resumeSubmissionId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESUME, "ResumeSubmissionService.read"));
        if (!resumeSubmission.getUserId().equals(userId)) throw new CustomException(ErrorCode.FORBIDDEN, "ResumeSubmissionService.read");
        return ResumeSubmissionResponse.from(resumeSubmission);
    }

    public void modify(ResumeSubmissionModifyRequest request, Long resumeSubmissionId, Long userId) {
        ResumeSubmission resumeSubmission = resumeSubmissionRepository.findById(resumeSubmissionId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "ResumeSubmissionService.modify"));
        if (!resumeSubmission.getUserId().equals(userId)) throw new CustomException(ErrorCode.FORBIDDEN, "ResumeSubmissionService.modify");
        resumeSubmission.update(request.name(), request.dateOfBirth(), request.gender(), request.longTermCareGrade(), request.majorDiseases(), request.specialNotes(), request.guardianName(), request.guardianContactInformation(), request.relationShip());
    }
}
