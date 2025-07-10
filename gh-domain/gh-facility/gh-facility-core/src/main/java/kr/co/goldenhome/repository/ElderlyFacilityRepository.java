package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.ElderlyFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ElderlyFacilityRepository extends JpaRepository<ElderlyFacility, Long> {
    @Query(
            value = "select * " +
                    "from elderly_facilities " +
                    "where elderly_facilities.facility_type = :facilityType " +
                    "limit :limit",
            nativeQuery = true
    )
    List<ElderlyFacility> findAllInfiniteScroll(@Param("facilityType") String facilityType, @Param("limit") Long limit);


    @Query(
            value = "select * " +
                    "from elderly_facilities " +
                    "where elderly_facilities.facility_type = :facilityType and elderly_facilities.id > :lastId " +
                    "limit :limit",
            nativeQuery = true
    )
    List<ElderlyFacility> findAllInfiniteScroll(@Param("facilityType") String facilityType, @Param("lastId") Long lastId, Long limit);
}
