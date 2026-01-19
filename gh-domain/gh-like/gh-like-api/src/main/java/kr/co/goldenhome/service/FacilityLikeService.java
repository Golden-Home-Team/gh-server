package kr.co.goldenhome.service;

import kr.co.goldenhome.FacilityEventManger;
import kr.co.goldenhome.entity.FacilityLike;
import kr.co.goldenhome.entity.FacilityLikeCount;
import kr.co.goldenhome.model.FacilityEvent;
import kr.co.goldenhome.model.FacilityEventType;
import kr.co.goldenhome.repository.FacilityLikeCountRepository;
import kr.co.goldenhome.repository.FacilityLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacilityLikeService {

    private final FacilityLikeRepository facilityLikeRepository;
    private final FacilityLikeCountRepository facilityLikeCountRepository;
    private final FacilityEventManger facilityEventManger;

    @Transactional
    public void like(Long facilityId, Long userId) {
        facilityLikeRepository.save(FacilityLike.create(facilityId, userId));
        int result = facilityLikeCountRepository.increase(facilityId);
        if (result == 0) {
            facilityLikeCountRepository.save(FacilityLikeCount.create(facilityId));
        }
        facilityEventManger.saveLog(FacilityEvent.createViewEvent(facilityId, FacilityEventType.LIKE));
    }

    @Transactional
    public void unlike(Long facilityId, Long userId) {
        facilityLikeRepository.findByFacilityIdAndUserId(facilityId, userId)
                .ifPresent(facilityLike -> {
                    int result = facilityLikeRepository.deleteByFacilityIdAndUserId(facilityId, userId);
                    if (result != 0) facilityLikeCountRepository.decrease(facilityId);
                });
        facilityEventManger.saveLog(FacilityEvent.createViewEvent(facilityId, FacilityEventType.DISLIKE));
    }
}
