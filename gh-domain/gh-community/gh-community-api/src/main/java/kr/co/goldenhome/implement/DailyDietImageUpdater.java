package kr.co.goldenhome.implement;

import kr.co.goldenhome.DailyDietImageApi;
import kr.co.goldenhome.ImageInfo;
import kr.co.goldenhome.dto.DailyDietImageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyDietImageUpdater {

    private final DailyDietImageApi dailyDietImageApi;

    public void update(List<DailyDietImageInfo> dailyDietImageInfos, Long dailyDietId) {
        dailyDietImageApi.deleteAllByDailyDietId(dailyDietId);
        List<ImageInfo> imageInfos = dailyDietImageInfos.stream().map(dailyDietImageInfo -> ImageInfo.create(dailyDietImageInfo.dailyDietType(), dailyDietImageInfo.formattedImageName())).toList();
        dailyDietImageApi.saveAll(dailyDietId, imageInfos);
    }


}
