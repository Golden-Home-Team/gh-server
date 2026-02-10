package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.ReviewStatistic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ReviewStatisticRepository extends JpaRepository<ReviewStatistic, Long> {

    @Query("select r.facilityId from ReviewStatistic r order by r.count desc")
    List<Long> findTopReviewCountFacilityIds(Pageable pageable);

    @Query("select r.facilityId from ReviewStatistic r order by r.averageScore desc")
    List<Long> findTopReviewAvgScoreFacilityIds(Pageable pageable);
}
