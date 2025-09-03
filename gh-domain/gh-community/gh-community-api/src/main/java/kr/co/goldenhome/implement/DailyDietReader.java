package kr.co.goldenhome.implement;

import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.entity.DailyDiet;
import kr.co.goldenhome.repository.DailyDietRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DailyDietReader {

    private final DailyDietRepository dailyDietRepository;

    public Long getLatest(Long facilityId) {
        Optional<DailyDiet> dailyDietOptional = dailyDietRepository.findTopByFacilityIdOrderByCreatedAtDesc(facilityId);
        return dailyDietOptional.map(DailyDiet::getId).orElse(null);
    }

    public DailyDiet get(Long dailyDietId) {
        return dailyDietRepository.findById(dailyDietId).orElseThrow(() -> new CustomException(ErrorCode.DAILY_DIET_NOT_FOUND, "DailyDietReader.get"));
    }
}
