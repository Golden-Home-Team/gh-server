package kr.co.goldenhome.submission.dto;

import kr.co.goldenhome.FacilityApiResponse;
import kr.co.goldenhome.entity.ResumeSubmission;
import kr.co.goldenhome.enums.AdmissionStatus;
import kr.co.goldenhome.enums.Gender;
import kr.co.goldenhome.enums.LongTermCareGrade;
import kr.co.goldenhome.enums.Relationship;

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
         String specialNotes,
         String guardianName,
         String guardianContactInformation,
         Relationship relationship,
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
                resumeSubmission.getSpecialNotes(),
                resumeSubmission.getGuardianName(),
                resumeSubmission.getGuardianContactInformation(),
                resumeSubmission.getRelationship(),
                resumeSubmission.getSubmitTime(),
                resumeSubmission.getStatus()
        );
    }
}
