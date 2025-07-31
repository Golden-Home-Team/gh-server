package kr.co.goldenhome.resume.dto;

import kr.co.goldenhome.entity.Resume;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ResumeResponse(
         Long id,
         Long userId,
         String name,
         LocalDate dateOfBirth,
         String gender,
         String longTermCareGrade,
         String majorDiseases,
         String specialNotes,
         String guardianName,
         String guardianContactInformation,
         String relationShip,
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
                resume.getLongTermCareGrade(),
                resume.getMajorDiseases(),
                resume.getSpecialNotes(),
                resume.getGuardianName(),
                resume.getGuardianContactInformation(),
                resume.getRelationship(),
                resume.getFacilityType(),
                resume.getUpdatedAt()
        );
    }
}
