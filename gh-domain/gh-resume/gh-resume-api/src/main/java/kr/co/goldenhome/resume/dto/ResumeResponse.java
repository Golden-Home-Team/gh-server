package kr.co.goldenhome.resume.dto;

import kr.co.goldenhome.entity.Resume;
import kr.co.goldenhome.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ResumeResponse(
         Long id,
         Long userId,
         String name,
         LocalDate dateOfBirth,
         Gender gender,
         PhysicalCondition physicalCondition,
         LongTermCareGrade longTermCareGrade,
         HealthInsurance healthInsurance,
         String specialNotes,
         String guardianName,
         String guardianContactInformation,
         Relationship relationship,
         String facilityType,
         LocalDateTime updatedAt
) {
    public static ResumeResponse from(Resume resume) {
        return new ResumeResponse(
                resume.getId(),
                resume.getUserId(),
                resume.getName(),
                resume.getDateOfBirth(),
                resume.getGender(),
                resume.getPhysicalCondition(),
                resume.getLongTermCareGrade(),
                resume.getHealthInsurance(),
                resume.getSpecialNotes(),
                resume.getGuardianName(),
                resume.getGuardianContactInformation(),
                resume.getRelationship(),
                resume.getFacilityType(),
                resume.getUpdatedAt()
        );
    }
}
