package kr.co.goldenhome;

import java.util.List;

public interface DailyShotImageApi {
    void saveAll(Long dailyShotId, List<DailyShotImageInfo> dailyShotImageInfoList);
    void deleteAllByDailyShotId(Long dailyShotId);
    DailyShotImageApiResponse getLatestByDailyShotId(Long dailyShotId);
    List<DailyShotImageApiResponse> getLatestImagesByDailyShotId(Long dailyShotId);
}
