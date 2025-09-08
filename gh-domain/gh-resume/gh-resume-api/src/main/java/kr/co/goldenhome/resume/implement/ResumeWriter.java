package kr.co.goldenhome.resume.implement;

import kr.co.goldenhome.entity.Resume;
import kr.co.goldenhome.enums.*;
import kr.co.goldenhome.repository.ResumeRepository;
import kr.co.goldenhome.resume.dto.ResumeCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResumeWriter {

    private final ResumeRepository resumeRepository;

    public void write(ResumeCreateRequest request, Long userId) {
        resumeRepository.save(
                Resume.create(
                        userId,
                        request.name(),
                        request.dateOfBirth(),
                        Gender.valueOf(request.gender()),
                        PhysicalCondition.valueOf(request.physicalCondition()),
                        LongTermCareGrade.valueOf(request.longTermCareGrade()),
                        HealthInsurance.valueOf(request.healthInsurance()),
                        request.specialNotes(),
                        request.guardianName(),
                        request.guardianContactInformation(),
                        Relationship.valueOf(request.relationship()),
                        request.facilityType(),
                        AdmissionTimeFrame.valueOf(request.admissionTimeFrame())
                )
        );
    }
}
