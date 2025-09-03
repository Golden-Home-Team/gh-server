package kr.co.goldenhome;

import kr.co.goldenhome.implement.FacilityViewCountBackUpManager;
import kr.co.goldenhome.repository.FacilityViewCountRepository;
import kr.co.goldenhome.repository.FacilityViewDistributedLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ViewApiImpl implements ViewApi {

    private final FacilityViewCountRepository facilityViewCountRepository;
    private final FacilityViewCountBackUpManager facilityViewCountBackUpManager;
    private final FacilityViewDistributedLockRepository facilityViewDistributedLockRepository;
    private static final int BACK_UP_BATCH_SIZE = 100;
    private static final Duration TTL = Duration.ofMinutes(10);

    @Override
    public Long increase(Long facilityId, Long userId) {
        if (!facilityViewDistributedLockRepository.lock(facilityId, userId, TTL)) {
            return facilityViewCountRepository.read(facilityId);
        }
        Long count = facilityViewCountRepository.increase(facilityId);
        if (count % BACK_UP_BATCH_SIZE == 0) {
            facilityViewCountBackUpManager.backUp(facilityId, userId);
        }
        return count;
    }
}
