package kr.co.goldenhome.submission.dto;

import java.time.LocalDate;

public record ResumeSubmissionModifyRequest(String name, LocalDate dateOfBirth, String gender, String longTermCareGrade, String majorDiseases, String specialNotes, String guardianName, String guardianContactInformation, String relationShip) {
}
