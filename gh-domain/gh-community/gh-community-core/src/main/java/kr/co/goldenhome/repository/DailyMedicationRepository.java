package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.DailyMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface DailyMedicationRepository extends JpaRepository<DailyMedication, Long> {

    @Query(
            value = "select * from daily_medications " +
                    "where facility_id = :facilityId " +
                    "and DAYOFWEEK(record_date) = :dayOfWeek + 1 " +
                    "ORDER BY created_at DESC",
            nativeQuery = true
    )
    DailyMedication getLatestByFacilityIdAndDayOfWeek(@Param("facilityId") Long facilityId, @Param("dayOfWeek") int dayOfWeek);
}
