package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.FacilityEventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FacilityEventLogRepository extends JpaRepository<FacilityEventLog, Long> {
    @Modifying
    @Query(
            value = "update facility_event_logs " +
                    "set is_published = true " +
                    "where event_id = :eventId",
            nativeQuery = true
    )
    void publish(@Param("eventId") String eventId);
}
