package kr.co.goldenhome.implement;

import kr.co.goldenhome.DailyShotImageApi;
import kr.co.goldenhome.DailyShotImageInfo;
import kr.co.goldenhome.dto.DailyShotImageInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyShotImageUpdater {

    private final DailyShotImageApi dailyShotImageApi;

    public void update(List<DailyShotImageInfoRequest> dailyShotImageInfoRequests, Long dailyShotId) {
        dailyShotImageApi.deleteAllByDailyShotId(dailyShotId);
        List<DailyShotImageInfo> dailyShotImageInfoList = dailyShotImageInfoRequests.stream().map(request -> DailyShotImageInfo.create(request.dailyShotType(), request.formattedImageName())).toList();
        dailyShotImageApi.saveAll(dailyShotId, dailyShotImageInfoList);
    }
}
