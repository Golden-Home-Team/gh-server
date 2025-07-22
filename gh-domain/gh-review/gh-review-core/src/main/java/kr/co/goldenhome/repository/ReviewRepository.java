package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * AND (
 *     (:lastId IS NULL) OR
 *     (:sort = 'score' AND (...)) OR
 *     (:sort != 'score' AND reviews.id < :lastId)
 * )
 * 1. reviews.facility_id = :facilityId AND ///... lastId == null ? true, True or... 이므로 이후 조건 무시
 * 2. sort = 'score' 인 경우
 * 2-1. 현제 score 가 이전 score 보다 낮은 경우
 * 2-2. 현제 score 와 이전 score 가 동점이면 id 내림차순
 * 3. 단순 내림차순
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query(
            value = "SELECT * " +
                    "FROM reviews " +
                    "WHERE reviews.facility_id = :facilityId " +
                    "AND (" +
                    "    (:lastId IS NULL) OR " + // 1
                    "    (:sort = 'score' AND (" + // 2
                    "        reviews.score < :lastScore " + // 2-1
                    "        OR (reviews.score = :lastScore AND reviews.id < :lastId) " + // 2-2
                    "    )) " +
                    "    OR " +
                    "    (:sort != 'score' AND reviews.id < :lastId) " + // 3
                    ") " +
                    "ORDER BY " +
                    "CASE WHEN :sort = 'score' THEN reviews.score END DESC, " +
                    "reviews.id DESC " +
                    "LIMIT :limit",
            nativeQuery = true
    )
    List<Review> findAllInfiniteScroll(
            @Param("facilityId") Long facilityId,
            @Param("lastId") Long lastId,
            @Param("lastScore") Integer lastScore,
            @Param("limit") Long limit,
            @Param("sort") String sort
    );

}
