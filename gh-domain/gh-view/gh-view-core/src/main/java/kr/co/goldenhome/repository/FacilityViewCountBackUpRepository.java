package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.FacilityViewCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FacilityViewCountBackUpRepository extends JpaRepository<FacilityViewCount, Long> {

    @Modifying
    @Query(
            value = "update facility_view_counts set view_count = :viewCount " +
                    "where facility_id = :facilityId and view_count < :viewCount",
            nativeQuery = true
    )
    int updateViewCount(
            @Param("facilityId") Long facilityId,
            @Param("viewCount") Long viewCount
    );
}
