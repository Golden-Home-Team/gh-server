package kr.co.goldenhome.dto;

import kr.co.goldenhome.entity.Resume;

import java.time.LocalDate;

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
         String relationShip
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
                resume.getRelationship()
        );
    }
}
