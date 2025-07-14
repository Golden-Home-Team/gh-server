package kr.co.goldenhome.dto;

import java.time.LocalDate;

public record ResumeModifyRequest(String name, LocalDate dateOfBirth, String gender, String longTermCareGrade, String majorDiseases, String specialNotes, String guardianName, String guardianContactInformation, String relationShip) {
}
