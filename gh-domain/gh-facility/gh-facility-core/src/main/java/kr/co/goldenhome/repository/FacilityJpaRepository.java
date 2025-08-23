package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityJpaRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByIdIn(List<Long> facilityIds);
}
