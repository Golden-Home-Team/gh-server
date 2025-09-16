package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
