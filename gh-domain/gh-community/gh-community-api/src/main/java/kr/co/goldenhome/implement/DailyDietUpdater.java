package kr.co.goldenhome.implement;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.entity.DailyDiet;
import kr.co.goldenhome.repository.DailyDietRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyDietUpdater {

    private final DailyDietRepository dailyDietRepository;

    public void update(Long dailyDietId, String content) {
        DailyDiet dailyDiet = dailyDietRepository.findById(dailyDietId).orElseThrow(() -> new CustomException(ErrorCode.DAILY_DIET_NOT_FOUND, "DailyDietService.update"));
        dailyDiet.update(content);
    }
}
