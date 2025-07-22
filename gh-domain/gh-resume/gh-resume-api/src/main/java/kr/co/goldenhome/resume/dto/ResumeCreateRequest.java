package kr.co.goldenhome.resume.dto;

import java.time.LocalDate;

public record ResumeCreateRequest(String name, LocalDate dateOfBirth, String gender, String longTermCareGrade, String majorDiseases, String specialNotes, String guardianName, String guardianContactInformation, String relationShip) {
}
