package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.enums.PhysicalCondition;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "resume_submission_physical_conditions")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeSubmissionPhysicalCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long resumeSubmissionId;
    @Enumerated(EnumType.STRING)
    private PhysicalCondition physicalCondition;

    @Builder
    private ResumeSubmissionPhysicalCondition(Long id, Long resumeSubmissionId, PhysicalCondition physicalCondition) {
        this.id = id;
        this.resumeSubmissionId = resumeSubmissionId;
        this.physicalCondition = physicalCondition;
    }

    public static ResumeSubmissionPhysicalCondition create(Long resumeSubmissionId, PhysicalCondition physicalCondition) {
        return ResumeSubmissionPhysicalCondition.builder()
                .resumeSubmissionId(resumeSubmissionId)
                .physicalCondition(physicalCondition)
                .build();
    }
}
