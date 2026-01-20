package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.RecentView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecentViewRepository extends JpaRepository<RecentView, Long> {
    Optional<RecentView> findByUserIdAndFacilityId(Long userId, Long facilityId);
    List<RecentView> findByUserId(Long userId);
}
