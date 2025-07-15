package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.enums.AdmissionStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private Long userId;
    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private String longTermCareGrade;
    private String majorDiseases;
    private String specialNotes;
    private String guardianName;
    private String guardianContactInformation;
    private String relationship;
    private LocalDateTime submitTime;
    @Enumerated(EnumType.STRING)
    private AdmissionStatus status;

    @Builder
    private ResumeSubmission(Long id, Long resumeId, Long facilityId, Long userId, String name, LocalDate dateOfBirth, String gender, String longTermCareGrade, String majorDiseases, String specialNotes, String guardianName, String guardianContactInformation, String relationship, LocalDateTime submitTime, AdmissionStatus status) {
        this.id = id;
        this.resumeId = resumeId;
        this.facilityId = facilityId;
        this.userId = userId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.longTermCareGrade = longTermCareGrade;
        this.majorDiseases = majorDiseases;
        this.specialNotes = specialNotes;
        this.guardianName = guardianName;
        this.guardianContactInformation = guardianContactInformation;
        this.relationship = relationship;
        this.submitTime = submitTime;
        this.status = status;
    }

    public static ResumeSubmission create(Resume resume, Long facilityId) {
        return ResumeSubmission.builder()
                .resumeId(resume.getId())
                .facilityId(facilityId)
                .userId(resume.getUserId())
                .name(resume.getName())
                .dateOfBirth(resume.getDateOfBirth())
                .gender(resume.getGender())
                .longTermCareGrade(resume.getLongTermCareGrade())
                .majorDiseases(resume.getMajorDiseases())
                .specialNotes(resume.getSpecialNotes())
                .guardianName(resume.getGuardianName())
                .guardianContactInformation(resume.getGuardianContactInformation())
                .relationship(resume.getRelationship())
                .submitTime(LocalDateTime.now())
                .status(AdmissionStatus.PENDING_REVIEW)
                .build();
    }

    public void update(String name, LocalDate dateOfBirth, String gender, String longTermCareGrade, String majorDiseases, String specialNotes, String guardianName, String guardianContactInformation, String relationShip) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.longTermCareGrade = longTermCareGrade;
        this.majorDiseases = majorDiseases;
        this.specialNotes = specialNotes;
        this.guardianName = guardianName;
        this.guardianContactInformation = guardianContactInformation;
        this.relationship = relationShip;
    }
}
