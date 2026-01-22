package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.ResumeSubmissionPhysicalCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeSubmissionPhysicalConditionRepository extends JpaRepository<ResumeSubmissionPhysicalCondition, Long> {
    List<ResumeSubmissionPhysicalCondition> findByResumeSubmissionId(Long resumeSubmissionId);
}
