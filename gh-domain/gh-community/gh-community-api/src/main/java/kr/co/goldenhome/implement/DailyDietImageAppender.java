package kr.co.goldenhome.implement;

import kr.co.goldenhome.DailyDietImageApi;
import kr.co.goldenhome.DailyDietImageInfo;
import kr.co.goldenhome.dto.DailyDietImageInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyDietImageAppender {

    private final DailyDietImageApi dailyDietImageApi;

    public void saveAll(Long dailyDietId, List<DailyDietImageInfoRequest> dailyDietImageInfoRequests) {
        List<DailyDietImageInfo> imageInfos = dailyDietImageInfoRequests.stream().map(request -> DailyDietImageInfo.create(request.dailyDietType(), request.formattedImageName())).toList();
        dailyDietImageApi.saveAll(dailyDietId, imageInfos);
    }
}
