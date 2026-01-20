package kr.co.goldenhome.service;

import kr.co.goldenhome.entity.RecentView;
import kr.co.goldenhome.repository.RecentViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecentViewService {

    private final RecentViewRepository recentViewRepository;

    @Transactional
    public void saveOrUpdate(Long userId, Long facilityId) {
        Optional<RecentView> recentViewOptional = recentViewRepository.findByUserIdAndFacilityId(userId, facilityId);
        if (recentViewOptional.isPresent()) recentViewOptional.get().view();
        else {
            RecentView recentView = RecentView.create(userId, facilityId);
            recentViewRepository.save(recentView);
        }
    }
}
