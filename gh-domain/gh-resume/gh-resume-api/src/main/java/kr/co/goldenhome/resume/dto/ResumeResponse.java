package kr.co.goldenhome.resume.dto;

import kr.co.goldenhome.entity.Resume;
import kr.co.goldenhome.entity.ResumePhysicalCondition;
import kr.co.goldenhome.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ResumeResponse(
        Long id,
        Long userId,
        String name,
        LocalDate dateOfBirth,
        Gender gender,
        List<PhysicalCondition> physicalCondition,
        LongTermCareGrade longTermCareGrade,
        HealthInsurance healthInsurance,
        String specialNotes,
        String guardianName,
        String guardianContactInformation,
        Relationship relationship,
        String facilityType,
        LocalDateTime updatedAt,
        AdmissionTimeFrame admissionTimeFrame,
        String otherRelationship
) {
    public static ResumeResponse of(Resume resume, List<ResumePhysicalCondition> resumePhysicalConditions) {
        List<PhysicalCondition> physicalConditionList = resumePhysicalConditions.stream().map(ResumePhysicalCondition::getPhysicalCondition).toList();
        return new ResumeResponse(
                resume.getId(),
                resume.getUserId(),
                resume.getName(),
                resume.getDateOfBirth(),
                resume.getGender(),
                physicalConditionList,
                resume.getLongTermCareGrade(),
                resume.getHealthInsurance(),
                resume.getSpecialNotes(),
                resume.getGuardianName(),
                resume.getGuardianContactInformation(),
                resume.getRelationship(),
                resume.getFacilityType(),
                resume.getUpdatedAt(),
                resume.getAdmissionTimeFrame(),
                resume.getOtherRelationship()
        );
    }
}
