package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.DailyRehabilitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DailyRehabilitationRepository extends JpaRepository<DailyRehabilitation, Long> {

    @Query(
            value = "select * from daily_rehabilitations " +
                    "where facility_id = :facilityId " +
                    "and DAYOFWEEK(record_date) = :dayOfWeek + 1",
            nativeQuery = true
    )
    DailyRehabilitation getLatestByFacilityIdAndDayOfWeek(@Param("facilityId") Long facilityId, @Param("dayOfWeek") int dayOfWeek);
}
