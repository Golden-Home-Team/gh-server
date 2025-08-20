package kr.co.goldenhome;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DailyDietImageApiTestImpl implements DailyDietImageApi{
    @Override
    public void saveAll(Long dailyDietId, List<DailyDietImageInfo> dailyDietImageInfoList) {

    }

    @Override
    public void deleteAllByDailyDietId(Long dailyDietId) {

    }

    @Override
    public DailyDietImageApiResponse getLatest(Long dailyDietId) {
        return null;
    }

    @Override
    public List<DailyDietImageApiResponse> get(Long dailyDietId) {
        return List.of();
    }
}
