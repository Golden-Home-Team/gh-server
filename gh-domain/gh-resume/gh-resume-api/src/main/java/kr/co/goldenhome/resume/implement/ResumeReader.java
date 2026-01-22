package kr.co.goldenhome.resume.implement;

import kr.co.goldenhome.entity.ResumePhysicalCondition;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.entity.Resume;
import kr.co.goldenhome.repository.ResumePhysicalConditionRepository;
import kr.co.goldenhome.repository.ResumeRepository;
import kr.co.goldenhome.resume.dto.ResumeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResumeReader {

    private final ResumeRepository resumeRepository;
    private final ResumePhysicalConditionRepository resumePhysicalConditionRepository;

    public ResumeResponse read(Long userId) {
        Resume resume = resumeRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "ResumeReader.read"));
        List<ResumePhysicalCondition> physicalConditions = resumePhysicalConditionRepository.findByResumeId(resume.getId());
        return ResumeResponse.of(resume, physicalConditions);
    }

}
