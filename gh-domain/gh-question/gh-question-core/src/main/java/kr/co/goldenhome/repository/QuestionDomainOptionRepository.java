package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.QuestionDomainOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionDomainOptionRepository extends JpaRepository<QuestionDomainOption, Long> {
    List<QuestionDomainOption> findByQuestionDomainId(Long questionDomainId);
    List<QuestionDomainOption> findByIdIn(List<Long> ids);
}
