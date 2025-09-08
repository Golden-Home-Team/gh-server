package kr.co.goldenhome.resume.dto;

import java.time.LocalDate;

public record ResumeModifyRequest(String name,
                                  LocalDate dateOfBirth,
                                  String gender,
                                  String physicalCondition,
                                  String longTermCareGrade,
                                  String healthInsurance,
                                  String specialNotes,
                                  String guardianName,
                                  String guardianContactInformation,
                                  String relationship,
                                  String facilityType,
                                  String admissionTimeFrame) {
}
