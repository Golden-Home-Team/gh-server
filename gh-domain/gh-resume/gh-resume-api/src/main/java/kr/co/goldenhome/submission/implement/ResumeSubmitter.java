package kr.co.goldenhome.submission.implement;

import kr.co.goldenhome.entity.ResumePhysicalCondition;
import kr.co.goldenhome.entity.ResumeSubmissionPhysicalCondition;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.entity.Resume;
import kr.co.goldenhome.entity.ResumeSubmission;
import kr.co.goldenhome.repository.ResumePhysicalConditionRepository;
import kr.co.goldenhome.repository.ResumeRepository;
import kr.co.goldenhome.repository.ResumeSubmissionPhysicalConditionRepository;
import kr.co.goldenhome.repository.ResumeSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResumeSubmitter {

    private final ResumeRepository resumeRepository;
    private final ResumeSubmissionRepository resumeSubmissionRepository;
    private final ResumePhysicalConditionRepository resumePhysicalConditionRepository;
    private final ResumeSubmissionPhysicalConditionRepository resumeSubmissionPhysicalConditionRepository;

    public void submit(Long facilityId, Long userId) {
        Resume resume = resumeRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_FOUND, "ResumeSubmitter.submit"));
        ResumeSubmission resumeSubmission = resumeSubmissionRepository.save(ResumeSubmission.create(resume, facilityId));
        List<ResumePhysicalCondition> resumePhysicalConditions = resumePhysicalConditionRepository.findByResumeId(resume.getId());
        for (ResumePhysicalCondition resumePhysicalCondition : resumePhysicalConditions) {
            resumeSubmissionPhysicalConditionRepository.save(ResumeSubmissionPhysicalCondition.create(resumeSubmission.getId(), resumePhysicalCondition.getPhysicalCondition()));
        }

    }

}
