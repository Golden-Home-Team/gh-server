package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.CommunityUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityUserRepository extends JpaRepository<CommunityUser, Long> {
    Optional<CommunityUser> findByFacilityIdAndUserId(Long facilityId, Long userId);
}
