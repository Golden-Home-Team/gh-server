package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.FacilityPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityPhotoRepository extends JpaRepository<FacilityPhoto, Long> {
    List<FacilityPhoto> findByInstitutionSymbol(String institutionSymbol);
}
