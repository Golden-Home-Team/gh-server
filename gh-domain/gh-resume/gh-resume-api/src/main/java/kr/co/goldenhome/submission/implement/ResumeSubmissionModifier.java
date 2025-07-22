package kr.co.goldenhome.submission.implement;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.submission.dto.ResumeSubmissionModifyRequest;
import kr.co.goldenhome.entity.ResumeSubmission;
import kr.co.goldenhome.repository.ResumeSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResumeSubmissionModifier {

    private final ResumeSubmissionRepository resumeSubmissionRepository;

    public void modify(ResumeSubmissionModifyRequest request, Long resumeSubmissionId, Long userId) {
        ResumeSubmission resumeSubmission = resumeSubmissionRepository.findById(resumeSubmissionId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "ResumeSubmissionService.modify"));
        if (!resumeSubmission.getUserId().equals(userId)) throw new CustomException(ErrorCode.FORBIDDEN, "ResumeSubmissionService.modify");
        resumeSubmission.update(request.name(), request.dateOfBirth(), request.gender(), request.longTermCareGrade(), request.majorDiseases(), request.specialNotes(), request.guardianName(), request.guardianContactInformation(), request.relationShip());
    }
}
