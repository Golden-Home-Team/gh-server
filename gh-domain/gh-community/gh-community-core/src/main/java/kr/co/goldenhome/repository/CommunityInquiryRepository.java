package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.CommunityInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityInquiryRepository extends JpaRepository<CommunityInquiry, Long> {

    @Query(
            value = "select * " +
                    "from community_inquiries " +
                    "where facility_id = :facilityId " +
                    "limit :limit",
            nativeQuery = true
    )
    List<CommunityInquiry> findAllInfiniteScroll(@Param("facilityId") Long facilityId, @Param("limit") Long limit);

    @Query(
            value = "select * " +
                    "from community_inquiries " +
                    "where facility_id = :facilityId and community_inquiries.id > :lastId " +
                    "limit :limit",
            nativeQuery = true
    )
    List<CommunityInquiry> findAllInfiniteScroll(@Param("facilityId") Long facilityId, @Param("lastId") Long lastId, @Param("limit") Long limit);
}
