package kr.co.goldenhome.submission.dto;

import kr.co.goldenhome.FacilityApiResponse;
import kr.co.goldenhome.entity.ResumeSubmission;
import kr.co.goldenhome.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ResumeSubmissionResponse(
         Long id,
         Long resumeId,
         Long facilityId,
         String facilityName,
         String facilityAddress,
         String name,
         LocalDate dateOfBirth,
         Gender gender,
         LongTermCareGrade longTermCareGrade,
         PhysicalCondition physicalCondition,
         HealthInsurance healthInsurance,
         String specialNotes,
         String guardianName,
         String guardianContactInformation,
         Relationship relationship,
         LocalDateTime submitTime,
         AdmissionStatus admissionStatus
) {
    public static ResumeSubmissionResponse of(ResumeSubmission resumeSubmission, FacilityApiResponse facilityApiResponse) {
        return new ResumeSubmissionResponse(
                resumeSubmission.getId(),
                resumeSubmission.getResumeId(),
                resumeSubmission.getFacilityId(),
                facilityApiResponse.name(),
                facilityApiResponse.address(),
                resumeSubmission.getName(),
                resumeSubmission.getDateOfBirth(),
                resumeSubmission.getGender(),
                resumeSubmission.getLongTermCareGrade(),
                resumeSubmission.getPhysicalCondition(),
                resumeSubmission.getHealthInsurance(),
                resumeSubmission.getSpecialNotes(),
                resumeSubmission.getGuardianName(),
                resumeSubmission.getGuardianContactInformation(),
                resumeSubmission.getRelationship(),
                resumeSubmission.getSubmitTime(),
                resumeSubmission.getAdmissionStatus()
        );
    }
}
