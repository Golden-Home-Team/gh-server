package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.DailyDiet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DailyDietRepository extends JpaRepository<DailyDiet, Long> {
    List<DailyDiet> findByFacilityId(Long facilityId);
    Optional<DailyDiet> findTopByFacilityIdOrderByCreatedAtDesc(Long facilityId);
}
