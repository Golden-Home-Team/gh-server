package kr.co.goldenhome.entity;

import jakarta.persistence.*;
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
    private String gender;
    private String longTermCareGrade;
    private String majorDiseases;
    private String specialNotes;
    private String guardianName;
    private String guardianContactInformation;
    private String relationship;
    private String facilityType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private Resume(Long id, Long userId, String name, LocalDate dateOfBirth, String gender, String longTermCareGrade, String majorDiseases, String specialNotes, String guardianName, String guardianContactInformation, String relationship, String facilityType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
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
        this.facilityType = facilityType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Resume create(Long userId, String name, LocalDate dateOfBirth, String gender, String longTermCareGrade, String majorDiseases, String specialNotes, String guardianName, String guardianContactInformation, String relationship, String facilityType) {
        return Resume.builder()
                .userId(userId)
                .name(name)
                .dateOfBirth(dateOfBirth)
                .gender(gender)
                .longTermCareGrade(longTermCareGrade)
                .majorDiseases(majorDiseases)
                .specialNotes(specialNotes)
                .guardianName(guardianName)
                .guardianContactInformation(guardianContactInformation)
                .relationship(relationship)
                .facilityType(facilityType)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void update(String name, LocalDate dateOfBirth, String gender, String longTermCareGrade, String majorDiseases, String specialNotes, String guardianName, String guardianContactInformation, String relationship, String facilityType) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.longTermCareGrade = longTermCareGrade;
        this.majorDiseases = majorDiseases;
        this.specialNotes = specialNotes;
        this.guardianName = guardianName;
        this.guardianContactInformation = guardianContactInformation;
        this.relationship = relationship;
        this.facilityType = facilityType;
        this.updatedAt = LocalDateTime.now();
    }
}
