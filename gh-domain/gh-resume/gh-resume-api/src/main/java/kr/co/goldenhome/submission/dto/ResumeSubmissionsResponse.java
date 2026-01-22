package kr.co.goldenhome.submission.dto;

import kr.co.goldenhome.FacilityApiResponse;
import kr.co.goldenhome.entity.ResumeSubmission;
import kr.co.goldenhome.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ResumeSubmissionsResponse(
        Long id,
        Long resumeId,
        Long facilityId,
        String facilityName,
        String facilityAddress,
        String name,
        LocalDate dateOfBirth,
        Gender gender,
        LongTermCareGrade longTermCareGrade,
        HealthInsurance healthInsurance,
        String specialNotes,
        String guardianName,
        String guardianContactInformation,
        Relationship relationship,
        LocalDateTime submitTime,
        AdmissionStatus admissionStatus,
        String otherRelationship
) {

    public static ResumeSubmissionsResponse of(ResumeSubmission resumeSubmission, FacilityApiResponse facilityApiResponse) {
        return new ResumeSubmissionsResponse(
                resumeSubmission.getId(),
                resumeSubmission.getResumeId(),
                resumeSubmission.getFacilityId(),
                facilityApiResponse.name(),
                facilityApiResponse.address(),
                resumeSubmission.getName(),
                resumeSubmission.getDateOfBirth(),
                resumeSubmission.getGender(),
                resumeSubmission.getLongTermCareGrade(),
                resumeSubmission.getHealthInsurance(),
                resumeSubmission.getSpecialNotes(),
                resumeSubmission.getGuardianName(),
                resumeSubmission.getGuardianContactInformation(),
                resumeSubmission.getRelationship(),
                resumeSubmission.getSubmitTime(),
                resumeSubmission.getAdmissionStatus(),
                resumeSubmission.getOtherRelationship()
        );
    }
}
