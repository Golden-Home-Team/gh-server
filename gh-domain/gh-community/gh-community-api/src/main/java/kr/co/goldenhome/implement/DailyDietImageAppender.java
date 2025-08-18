package kr.co.goldenhome.implement;

import kr.co.goldenhome.DailyDietImageApi;
import kr.co.goldenhome.ImageInfo;
import kr.co.goldenhome.dto.DailyDietImageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyDietImageAppender {

    private final DailyDietImageApi dailyDietImageApi;

    public void saveAll(Long dailyDietId, List<DailyDietImageInfo> dailyDietImageInfos) {
        List<ImageInfo> imageInfos = dailyDietImageInfos.stream().map(imageInfo -> ImageInfo.create(imageInfo.dailyDietType(), imageInfo.formattedImageName())).toList();
        dailyDietImageApi.saveAll(dailyDietId, imageInfos);
    }
}
