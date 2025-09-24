package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.CommunityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunityUserRepository extends JpaRepository<CommunityUser, Long> {
    Optional<CommunityUser> findByFacilityIdAndUserId(Long facilityId, Long userId);
    @Query(
            value = "select *" +
                    "from community_users " +
                    "where facility_id =:facilityId and community_user_role = 'MANAGER' " +
                    "order by joined_at",
            nativeQuery = true
    )
    CommunityUser getManager(@Param("facilityId") Long facilityId);

    List<CommunityUser> findByUserId(Long userId);
}
