package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.FacilityLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FacilityLikeCountRepository extends JpaRepository<FacilityLikeCount, Long> {

    @Modifying
    @Query(
            value = "update facility_like_counts set like_count = like_count + 1 where facility_id = :facilityId",
            nativeQuery = true
    )
    int increase(@Param("facilityId") Long facilityId);

    @Modifying
    @Query(
            value = "update facility_like_counts set like_count = like_count - 1 where facility_id = :facilityId",
            nativeQuery = true
    )
    int decrease(@Param("facilityId") Long facilityId);
}
