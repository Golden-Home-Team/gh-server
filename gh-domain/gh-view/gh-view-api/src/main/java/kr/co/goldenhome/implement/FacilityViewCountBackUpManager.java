package kr.co.goldenhome.implement;

import kr.co.goldenhome.entity.FacilityViewCount;
import kr.co.goldenhome.repository.FacilityViewCountBackUpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FacilityViewCountBackUpManager {

    private final FacilityViewCountBackUpRepository facilityViewCountBackUpRepository;

    @Transactional
    public void backUp(Long facilityId, Long viewCount) {
        int result = facilityViewCountBackUpRepository.updateViewCount(facilityId, viewCount);
        if (result == 0) {
            facilityViewCountBackUpRepository.findById(facilityId)
                    .ifPresentOrElse(ignored -> {},
                            () -> facilityViewCountBackUpRepository.save(
                                    FacilityViewCount.create(facilityId, viewCount)
                            )
                    );
        }
    }
}
