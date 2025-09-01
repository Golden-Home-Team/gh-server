package kr.co.goldenhome;

import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.entity.DailyShotImage;
import kr.co.goldenhome.enums.DailyShotType;
import kr.co.goldenhome.repository.DailyShotImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyShotImageApiImpl implements DailyShotImageApi {

    private final DailyShotImageRepository dailyShotImageRepository;
    @Value("${aws.s3.base-url}")
    private String awsBaseUrl;

    @Override
    public void saveAll(Long dailyShotId, List<DailyShotImageInfo> dailyShotImageInfoList) {
        for (DailyShotImageInfo dailyShotImageInfo : dailyShotImageInfoList) {
            try {
                DailyShotType.valueOf(dailyShotImageInfo.dailyShotType());
            } catch (IllegalArgumentException e) {
                throw new CustomException(ErrorCode.INVALID_ENUM, "DailyShotImageApiImpl.saveAll");
            }
        }
        List<DailyShotImage> dailyShotImages = dailyShotImageInfoList.stream().map(imageInfo -> DailyShotImage.create(dailyShotId, DailyShotType.valueOf(imageInfo.dailyShotType()), awsBaseUrl+imageInfo.formattedImageName())).toList();
        dailyShotImageRepository.saveAll(dailyShotImages);
    }

    @Override
    public void deleteAllByDailyShotId(Long dailyShotId) {
        dailyShotImageRepository.deleteAllByDailyShotId(dailyShotId);
    }

    @Override
    public DailyShotImageApiResponse getLatestByDailyShotId(Long dailyShotId) {
        List<DailyShotImage> dailyShotImages = dailyShotImageRepository.findByDailyShotIdOrderByCreatedAtDesc(dailyShotId);
        if (dailyShotImages.isEmpty()) return null;
        DailyShotImage dailyShotImage = dailyShotImages.getFirst();
        return new DailyShotImageApiResponse(dailyShotImage.getId(), dailyShotImage.getImageUrl(), dailyShotImage.getCreatedAt());
    }

    @Override
    public List<DailyShotImageApiResponse> getLatestImagesByDailyShotId(Long dailyShotId) {
        return dailyShotImageRepository.findByDailyShotIdOrderByCreatedAtDesc(dailyShotId)
                .stream().map(dailyShotImage -> new DailyShotImageApiResponse(dailyShotImage.getId(), dailyShotImage.getImageUrl(), dailyShotImage.getCreatedAt())).toList();
    }


}
