package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.enums.*;
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
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private PhysicalCondition physicalCondition;
    @Enumerated(EnumType.STRING)
    private LongTermCareGrade longTermCareGrade;
    @Enumerated(EnumType.STRING)
    private HealthInsurance healthInsurance;
    private String specialNotes;
    private String guardianName;
    private String guardianContactInformation;
    @Enumerated(EnumType.STRING)
    private Relationship relationship;
    @Enumerated(EnumType.STRING)
    private AdmissionTimeFrame admissionTimeFrame;
    private LocalDateTime submitTime;
    @Enumerated(EnumType.STRING)
    private AdmissionStatus admissionStatus;
    private String otherRelationship;

    @Builder
    public ResumeSubmission(Long id, Long resumeId, Long facilityId, Long userId, String name, LocalDate dateOfBirth, Gender gender, PhysicalCondition physicalCondition, LongTermCareGrade longTermCareGrade, HealthInsurance healthInsurance, String specialNotes, String guardianName, String guardianContactInformation, Relationship relationship, AdmissionTimeFrame admissionTimeFrame, LocalDateTime submitTime, AdmissionStatus admissionStatus, String otherRelationship) {
        this.id = id;
        this.resumeId = resumeId;
        this.facilityId = facilityId;
        this.userId = userId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.physicalCondition = physicalCondition;
        this.longTermCareGrade = longTermCareGrade;
        this.healthInsurance = healthInsurance;
        this.specialNotes = specialNotes;
        this.guardianName = guardianName;
        this.guardianContactInformation = guardianContactInformation;
        this.relationship = relationship;
        this.admissionTimeFrame = admissionTimeFrame;
        this.submitTime = submitTime;
        this.admissionStatus = admissionStatus;
        this.otherRelationship = otherRelationship;
    }

    public static ResumeSubmission create(Resume resume, Long facilityId) {
        return ResumeSubmission.builder()
                .resumeId(resume.getId())
                .facilityId(facilityId)
                .userId(resume.getUserId())
                .name(resume.getName())
                .dateOfBirth(resume.getDateOfBirth())
                .gender(resume.getGender())
                .physicalCondition(resume.getPhysicalCondition())
                .longTermCareGrade(resume.getLongTermCareGrade())
                .healthInsurance(resume.getHealthInsurance())
                .specialNotes(resume.getSpecialNotes())
                .guardianName(resume.getGuardianName())
                .guardianContactInformation(resume.getGuardianContactInformation())
                .relationship(resume.getRelationship())
                .admissionTimeFrame(resume.getAdmissionTimeFrame())
                .submitTime(LocalDateTime.now())
                .admissionStatus(AdmissionStatus.PENDING_REVIEW)
                .otherRelationship(resume.getOtherRelationship())
                .build();
    }

    public boolean isOwnedBy(Long userId) {
        return this.userId.equals(userId);
    }
}
