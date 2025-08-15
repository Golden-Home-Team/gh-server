package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.CommunityNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityNoticeRepository extends JpaRepository<CommunityNotice, Long> {

    @Query(
            value = "select * " +
                    "from community_notices " +
                    "where community_notices.facility_id = :facilityId " +
                    "limit :limit",
            nativeQuery = true
    )
    List<CommunityNotice> findAllInfiniteScroll(@Param("facilityId") Long facilityId, @Param("limit") Long limit);

    @Query(
            value = "select * " +
                    "from community_notices " +
                    "where community_notices.facility_id = :facilityId and community_notices.id > :lastId " +
                    "limit :limit",
            nativeQuery = true
    )
    List<CommunityNotice> findAllInfiniteScroll(@Param("facilityId") Long facilityId, @Param("lastId") Long lastId, @Param("limit") Long limit);
}
