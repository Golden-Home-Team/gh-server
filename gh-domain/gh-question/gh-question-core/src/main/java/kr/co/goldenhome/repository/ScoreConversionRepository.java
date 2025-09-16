package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.ScoreConversion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScoreConversionRepository extends JpaRepository<ScoreConversion, Long> {
    Optional<ScoreConversion> findByQuestionDomainIdAndOriginalSum(Long questionDomainId, double originalSum);
}
