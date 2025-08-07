package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.ReviewCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewCountRepository extends JpaRepository<ReviewCount, Long> {
    @Modifying
    @Query(
            value = "update review_counts set review_count = review_count + 1 where facility_id = :facilityId",
            nativeQuery = true
    )
    int increase(@Param("facilityId") Long facilityId);

    @Modifying
    @Query(
            value = "update review_counts set review_count = review_count - 1 where facility_id = :facilityId",
            nativeQuery = true
    )
    int decrease(@Param("facilityId") Long facilityId);
}
