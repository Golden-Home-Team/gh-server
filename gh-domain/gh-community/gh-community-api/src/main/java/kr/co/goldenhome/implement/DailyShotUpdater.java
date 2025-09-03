package kr.co.goldenhome.implement;

import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.entity.DailyShot;
import kr.co.goldenhome.repository.DailyShotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyShotUpdater {

    private final DailyShotRepository dailyShotRepository;

    public void update(Long dailyShotId, String content) {
        DailyShot dailyShot = dailyShotRepository.findById(dailyShotId).orElseThrow(() -> new CustomException(ErrorCode.DAILY_SHOT_NOT_FOUND, "DailyShotUpdater.update"));
        dailyShot.update(content);
    }
}
