package kr.co.goldenhome.submission.implement;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.ElderlyFacilityApi;
import kr.co.goldenhome.entity.Resume;
import kr.co.goldenhome.entity.ResumeSubmission;
import kr.co.goldenhome.repository.ResumeRepository;
import kr.co.goldenhome.repository.ResumeSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResumeSubmitter {

    private final ResumeSubmissionRepository resumeSubmissionRepository;
    private final ResumeRepository resumeRepository;
    private final ElderlyFacilityApi elderlyFacilityApi;

    public void submit(Long facilityId, Long userId) {
        Resume resume = resumeRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_FOUND, "ResumeSubmissionService.create"));
        if (!elderlyFacilityApi.existsById(facilityId)) throw new CustomException(ErrorCode.FACILITY_NOT_FOUND, "ResumeSubmissionService.create");
        resumeSubmissionRepository.save(ResumeSubmission.create(resume, facilityId));
    }

}
