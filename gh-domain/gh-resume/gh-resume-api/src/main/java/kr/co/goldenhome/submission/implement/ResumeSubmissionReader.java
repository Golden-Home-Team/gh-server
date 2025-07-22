package kr.co.goldenhome.submission.implement;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.ElderlyFacilityApi;
import kr.co.goldenhome.entity.Resume;
import kr.co.goldenhome.entity.ResumeSubmission;
import kr.co.goldenhome.repository.ResumeRepository;
import kr.co.goldenhome.repository.ResumeSubmissionRepository;
import kr.co.goldenhome.submission.dto.ResumeSubmissionModifyRequest;
import kr.co.goldenhome.submission.dto.ResumeSubmissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResumeSubmissionReader {

    private final ResumeSubmissionRepository resumeSubmissionRepository;

    public ResumeSubmissionResponse read(Long resumeSubmissionId, Long userId) {
        ResumeSubmission resumeSubmission = resumeSubmissionRepository.findById(resumeSubmissionId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESUME, "ResumeSubmissionService.read"));
        if(!resumeSubmission.isOwnedBy(userId)) throw new CustomException(ErrorCode.FORBIDDEN, "ResumeSubmissionService.read");
        return ResumeSubmissionResponse.from(resumeSubmission);
    }

    public List<ResumeSubmission> readAll(Long userId, Long lastId, Long pageSize) {
        return lastId == null ?
                resumeSubmissionRepository.findAllInfiniteScroll(userId, pageSize) :
                resumeSubmissionRepository.findAllInfiniteScroll(userId, lastId, pageSize);
    }

}
