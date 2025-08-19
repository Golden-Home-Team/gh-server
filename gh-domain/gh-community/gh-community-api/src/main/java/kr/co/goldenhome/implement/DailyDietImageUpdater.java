package kr.co.goldenhome.implement;

import kr.co.goldenhome.DailyDietImageApi;
import kr.co.goldenhome.DailyDietImageInfo;
import kr.co.goldenhome.dto.DailyDietImageInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyDietImageUpdater {

    private final DailyDietImageApi dailyDietImageApi;

    public void update(List<DailyDietImageInfoRequest> dailyDietDailyDietImageInfoRequests, Long dailyDietId) {
        dailyDietImageApi.deleteAllByDailyDietId(dailyDietId);
        List<DailyDietImageInfo> dailyDietImageInfoList = dailyDietDailyDietImageInfoRequests.stream().map(request -> DailyDietImageInfo.create(request.dailyDietType(), request.formattedImageName())).toList();
        dailyDietImageApi.saveAll(dailyDietId, dailyDietImageInfoList);
    }


}
