package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.FacilityProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityProgramRepository extends JpaRepository<FacilityProgram, Long> {
    List<FacilityProgram> findByInstitutionSymbol(String institutionSymbol);
}
