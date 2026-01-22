package kr.co.goldenhome.submission.implement;

import kr.co.goldenhome.entity.ResumeSubmissionPhysicalCondition;
import kr.co.goldenhome.enums.PhysicalCondition;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.entity.ResumeSubmission;
import kr.co.goldenhome.repository.ResumeSubmissionPhysicalConditionRepository;
import kr.co.goldenhome.repository.ResumeSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResumeSubmissionReader {

    private final ResumeSubmissionRepository resumeSubmissionRepository;
    private final ResumeSubmissionPhysicalConditionRepository resumeSubmissionPhysicalConditionRepository;

    public ResumeSubmission read(Long resumeSubmissionId, Long userId) {
        ResumeSubmission resumeSubmission = resumeSubmissionRepository.findById(resumeSubmissionId).orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_FOUND, "ResumeSubmissionService.read"));
        if(!resumeSubmission.isOwnedBy(userId)) throw new CustomException(ErrorCode.FORBIDDEN, "ResumeSubmissionService.read");
        return resumeSubmission;
    }

    public List<PhysicalCondition> readPhysicalCondition(Long resumeSubmissionId) {
        return resumeSubmissionPhysicalConditionRepository.findByResumeSubmissionId(resumeSubmissionId).stream()
                .map(ResumeSubmissionPhysicalCondition::getPhysicalCondition)
                .toList();
    }

    public List<ResumeSubmission> readAll(Long userId, Long lastId, Long pageSize) {
        return lastId == null ?
                resumeSubmissionRepository.findAllInfiniteScroll(userId, pageSize) :
                resumeSubmissionRepository.findAllInfiniteScroll(userId, lastId, pageSize);
    }

}
