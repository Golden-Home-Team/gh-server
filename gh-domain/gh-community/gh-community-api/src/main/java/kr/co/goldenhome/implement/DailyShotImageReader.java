package kr.co.goldenhome.implement;

import kr.co.goldenhome.DailyShotImageApi;
import kr.co.goldenhome.DailyShotImageApiResponse;
import kr.co.goldenhome.dto.DailyShotImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyShotImageReader {

    private final DailyShotImageApi dailyShotImageApi;

    public DailyShotImageResponse getLatestByDailyShotId(Long dailyShotId) {
        DailyShotImageApiResponse apiResponse = dailyShotImageApi.getLatestByDailyShotId(dailyShotId);
        if (apiResponse == null) return DailyShotImageResponse.empty();
        return new DailyShotImageResponse(apiResponse.id(),apiResponse.imageUrl(),apiResponse.createdAt());
    }

    public List<DailyShotImageResponse> getImagesByDailyShotId(Long dailyShotId) {
        return dailyShotImageApi.getLatestImagesByDailyShotId(dailyShotId)
                .stream().map(apiResponse -> new DailyShotImageResponse(apiResponse.id(), apiResponse.imageUrl(), apiResponse.createdAt())).toList();
    }

}
