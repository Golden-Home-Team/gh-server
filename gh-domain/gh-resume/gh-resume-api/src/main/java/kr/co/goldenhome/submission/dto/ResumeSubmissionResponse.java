package kr.co.goldenhome.submission.dto;

import kr.co.goldenhome.FacilityApiResponse;
import kr.co.goldenhome.entity.ResumeSubmission;
import kr.co.goldenhome.enums.AdmissionStatus;

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
         String gender,
         String longTermCareGrade,
         String majorDiseases,
         String specialNotes,
         String guardianName,
         String guardianContactInformation,
         String relationship,
         LocalDateTime submitTime,
         AdmissionStatus status

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
                resumeSubmission.getMajorDiseases(),
                resumeSubmission.getSpecialNotes(),
                resumeSubmission.getGuardianName(),
                resumeSubmission.getGuardianContactInformation(),
                resumeSubmission.getRelationship(),
                resumeSubmission.getSubmitTime(),
                resumeSubmission.getStatus()
        );
    }
}
