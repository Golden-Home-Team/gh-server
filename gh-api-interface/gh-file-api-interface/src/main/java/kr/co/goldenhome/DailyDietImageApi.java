package kr.co.goldenhome;

import java.util.List;

public interface DailyDietImageApi {
    void saveAll(Long dailyDietId, List<ImageInfo> imageInfos);
    void deleteAllByDailyDietId(Long dailyDietId);
    DailyDietImageApiResponse getLatest(Long dailyDietId);
    List<DailyDietImageApiResponse> get(Long dailyDietId);
}
