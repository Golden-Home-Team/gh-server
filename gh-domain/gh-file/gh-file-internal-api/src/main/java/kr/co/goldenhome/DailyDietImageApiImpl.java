package kr.co.goldenhome;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.entity.DailyDietImage;
import kr.co.goldenhome.enums.DailyDietType;
import kr.co.goldenhome.repository.DailyDietImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DailyDietImageApiImpl implements DailyDietImageApi {

    private final DailyDietImageRepository dailyDietImageRepository;
    @Value("${aws.s3.base-url}")
    private String awsBaseUrl;

    @Override
    public void saveAll(Long dailyDietId, List<DailyDietImageInfo> dailyDietImageInfoList) {
        for (DailyDietImageInfo dailyDietImageInfo : dailyDietImageInfoList) {
            try {
                DailyDietType.valueOf(dailyDietImageInfo.dailyDietType());
            } catch (IllegalArgumentException e) {
                throw new CustomException(ErrorCode.INVALID_ENUM, "DailyDietImageApiImpl.saveAll");
            }
        }
        List<DailyDietImage> dailyDietImages = dailyDietImageInfoList.stream().map(imageInfo -> DailyDietImage.create(dailyDietId, DailyDietType.valueOf(imageInfo.dailyDietType()), imageInfo.formattedImageName(), awsBaseUrl + imageInfo.formattedImageName())).toList();
        dailyDietImageRepository.saveAll(dailyDietImages);
    }

    @Override
    public void deleteAllByDailyDietId(Long dailyDietId) {
        dailyDietImageRepository.deleteAllByDailyDietId(dailyDietId);
    }

    @Override
    public DailyDietImageApiResponse getLatest(Long dailyDietId) {
        Optional<DailyDietImage> dailyDietImageOptional = dailyDietImageRepository.findTopByDailyDietIdOrderByCreatedAtDesc(dailyDietId);
        return dailyDietImageOptional.map(dailyDietImage -> new DailyDietImageApiResponse(dailyDietImage.getId(),dailyDietImage.getDailyDietType().name(), dailyDietImage.getFormattedName(), dailyDietImage.getImageUrl(), dailyDietImage.getCreatedAt())).orElse(null);
    }

    @Override
    public List<DailyDietImageApiResponse> get(Long dailyDietId) {
       return dailyDietImageRepository.findByDailyDietId(dailyDietId)
                .stream().map(dailyDietImage -> new DailyDietImageApiResponse(dailyDietImage.getId(),dailyDietImage.getDailyDietType().name(), dailyDietImage.getFormattedName(), dailyDietImage.getImageUrl(), dailyDietImage.getCreatedAt())).toList();
    }
}
