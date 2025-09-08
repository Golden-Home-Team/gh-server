package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.enums.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "resumes", uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_id", name = "UQ_RESUMES_USER_ID")
})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    private String facilityType;
    @Enumerated(EnumType.STRING)
    private AdmissionTimeFrame admissionTimeFrame;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private Resume(Long id, Long userId, String name, LocalDate dateOfBirth, Gender gender, LongTermCareGrade longTermCareGrade, String specialNotes, String guardianName, String guardianContactInformation, String facilityType, PhysicalCondition physicalCondition, HealthInsurance healthInsurance, Relationship relationship, AdmissionTimeFrame admissionTimeFrame, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.physicalCondition = physicalCondition;
        this.longTermCareGrade = longTermCareGrade;
        this.specialNotes = specialNotes;
        this.guardianName = guardianName;
        this.guardianContactInformation = guardianContactInformation;
        this.facilityType = facilityType;
        this.healthInsurance = healthInsurance;
        this.relationship = relationship;
        this.admissionTimeFrame = admissionTimeFrame;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Resume create(Long userId, String name, LocalDate dateOfBirth, Gender gender, PhysicalCondition physicalCondition, LongTermCareGrade longTermCareGrade, HealthInsurance healthInsurance, String specialNotes, String guardianName, String guardianContactInformation, Relationship relationship, String facilityType, AdmissionTimeFrame admissionTimeFrame) {
        return Resume.builder()
                .userId(userId)
                .name(name)
                .dateOfBirth(dateOfBirth)
                .gender(gender)
                .physicalCondition(physicalCondition)
                .longTermCareGrade(longTermCareGrade)
                .healthInsurance(healthInsurance)
                .specialNotes(specialNotes)
                .guardianName(guardianName)
                .guardianContactInformation(guardianContactInformation)
                .relationship(relationship)
                .facilityType(facilityType)
                .admissionTimeFrame(admissionTimeFrame)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void update(String name, LocalDate dateOfBirth, Gender gender, PhysicalCondition physicalCondition, LongTermCareGrade longTermCareGrade, HealthInsurance healthInsurance, String specialNotes, String guardianName, String guardianContactInformation, Relationship relationShip, String facilityType, AdmissionTimeFrame admissionTimeFrame) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.physicalCondition = physicalCondition;
        this.longTermCareGrade = longTermCareGrade;
        this.healthInsurance = healthInsurance;
        this.specialNotes = specialNotes;
        this.guardianName = guardianName;
        this.guardianContactInformation = guardianContactInformation;
        this.facilityType = facilityType;
        this.relationship = relationShip;
        this.admissionTimeFrame = admissionTimeFrame;
        this.updatedAt = LocalDateTime.now();
    }
}
