package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.FacilityLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FacilityLikeRepository extends JpaRepository<FacilityLike, Long> {
    Optional<FacilityLike> findByFacilityIdAndUserId(Long facilityId, Long userId);
    int deleteByFacilityIdAndUserId(Long facilityId, Long userId);
    List<FacilityLike> findByUserId(Long userId);
}
