package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.enums.PhysicalCondition;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "resume_physical_conditions")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumePhysicalCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long resumeId;
    @Enumerated(EnumType.STRING)
    private PhysicalCondition physicalCondition;

    @Builder
    private ResumePhysicalCondition(Long id, Long resumeId, PhysicalCondition physicalCondition) {
        this.id = id;
        this.resumeId = resumeId;
        this.physicalCondition = physicalCondition;
    }

    public static ResumePhysicalCondition create(Long resumeId, PhysicalCondition physicalCondition) {
        return ResumePhysicalCondition.builder()
                .resumeId(resumeId)
                .physicalCondition(physicalCondition)
                .build();
    }
}
