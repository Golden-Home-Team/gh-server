package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.enums.AdmissionStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "resume_submissions")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long resumeId;
    private Long facilityId;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String longTermCareGrade;
    private String majorDiseases;
    private String specialNotes;
    private String guardianName;
    private String guardianContactInformation;
    private String relationShip;
    private LocalDateTime submitTime;
    private AdmissionStatus status;

    @Builder
    private ResumeSubmission(Long id, Long resumeId, Long facilityId, String name, String dateOfBirth, String gender, String longTermCareGrade, String majorDiseases, String specialNotes, String guardianName, String guardianContactInformation, String relationShip, LocalDateTime submitTime, AdmissionStatus status) {
        this.id = id;
        this.resumeId = resumeId;
        this.facilityId = facilityId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.longTermCareGrade = longTermCareGrade;
        this.majorDiseases = majorDiseases;
        this.specialNotes = specialNotes;
        this.guardianName = guardianName;
        this.guardianContactInformation = guardianContactInformation;
        this.relationShip = relationShip;
        this.submitTime = submitTime;
        this.status = status;
    }
}
