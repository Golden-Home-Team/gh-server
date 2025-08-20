package kr.co.goldenhome.implement;

import kr.co.goldenhome.entity.DailyShot;
import kr.co.goldenhome.repository.DailyShotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyShotReader {

    private final DailyShotRepository dailyShotRepository;

    public DailyShot getLatestByFacilityId(Long facilityId) {
        return dailyShotRepository.findTopByFacilityIdOrderByCreatedAtDesc(facilityId).orElse(null);
    }

    public DailyShot getLatestByFacilityIdAndDayOfWeek(Long facilityId, int dayOffWeek) {
        return dailyShotRepository.getByFacilityIdAndDayOfWeek(facilityId, dayOffWeek);
    }


}
