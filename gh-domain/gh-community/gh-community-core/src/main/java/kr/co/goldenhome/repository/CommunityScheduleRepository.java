package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.CommunitySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommunityScheduleRepository extends JpaRepository<CommunitySchedule, Long> {

    @Query(
            value = "SELECT * FROM community_schedules " +
                    "WHERE facility_id = :facilityId " +
                    "AND MONTH(record_date) = :month",
            nativeQuery = true
    )
    List<CommunitySchedule> getByFacilityIdAndMonth(Long facilityId, int month);
}
