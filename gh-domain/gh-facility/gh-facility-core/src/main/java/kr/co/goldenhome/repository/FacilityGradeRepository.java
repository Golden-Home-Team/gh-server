package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.FacilityGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityGradeRepository extends JpaRepository<FacilityGrade, Long> {
    FacilityGrade findTopByInstitutionSymbolOrderByEvaluationDateDesc(String institutionSymbol);
}
