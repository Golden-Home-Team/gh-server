package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.DailyShot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DailyShotRepository extends JpaRepository<DailyShot, Long> {
    Optional<DailyShot> findTopByFacilityIdOrderByCreatedAtDesc(Long facilityId);
    @Query(
            value = "select * from daily_shots " +
                    "where facility_id = :facilityId " +
                    "and DAYOFWEEK(record_date) = :dayOfWeek + 1 " +
                    "ORDER BY created_at DESC",
            nativeQuery = true
    )
    DailyShot getByFacilityIdAndDayOfWeek(@Param("facilityId") Long facilityId, @Param("dayOfWeek") int dayOfWeek);
}
