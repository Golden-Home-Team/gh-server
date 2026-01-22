package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.ResumePhysicalCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResumePhysicalConditionRepository extends JpaRepository<ResumePhysicalCondition, Long> {
    List<ResumePhysicalCondition> findByResumeId(Long resumeId);
    @Modifying
    @Query("delete from ResumePhysicalCondition r where r.resumeId = :resumeId")
    void deleteByResumeId(Long resumeId);
}
