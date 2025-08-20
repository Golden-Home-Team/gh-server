package kr.co.goldenhome;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DailyShotImageApiTestImpl implements DailyShotImageApi {
    @Override
    public void saveAll(Long dailyShotId, List<DailyShotImageInfo> dailyShotImageInfoList) {

    }

    @Override
    public void deleteAllByDailyShotId(Long dailyShotId) {

    }

    @Override
    public DailyShotImageApiResponse getLatestByDailyShotId(Long dailyShotId) {
        return null;
    }

    @Override
    public List<DailyShotImageApiResponse> getLatestImagesByDailyShotId(Long dailyShotId) {
        return List.of();
    }
}
