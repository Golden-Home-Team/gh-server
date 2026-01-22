package kr.co.goldenhome.resume.implement;

import kr.co.goldenhome.entity.ResumePhysicalCondition;
import kr.co.goldenhome.enums.*;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.entity.Resume;
import kr.co.goldenhome.repository.ResumePhysicalConditionRepository;
import kr.co.goldenhome.repository.ResumeRepository;
import kr.co.goldenhome.resume.dto.ResumeModifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResumeModifier {

    private final ResumeRepository resumeRepository;
    private final ResumePhysicalConditionRepository resumePhysicalConditionRepository;

    @Transactional
    public void modify(ResumeModifyRequest request, Long userId) {
        Resume resume = resumeRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "ResumeModifier.modify"));
        resume.update(
                request.name(),
                request.dateOfBirth(),
                Gender.valueOf(request.gender()),
                LongTermCareGrade.valueOf(request.longTermCareGrade()),
                HealthInsurance.valueOf(request.healthInsurance()),
                request.specialNotes(),
                request.guardianName(),
                request.guardianContactInformation(),
                Relationship.valueOf(request.relationship()),
                request.facilityType(),
                AdmissionTimeFrame.valueOf(request.admissionTimeFrame()),
                request.otherRelationship()
        );
        resumePhysicalConditionRepository.deleteByResumeId(resume.getId());
        for (String pc : request.physicalCondition()) {
            resumePhysicalConditionRepository.save(
                    ResumePhysicalCondition.create(resume.getId(), PhysicalCondition.valueOf(pc))
            );
        }

    }
}
