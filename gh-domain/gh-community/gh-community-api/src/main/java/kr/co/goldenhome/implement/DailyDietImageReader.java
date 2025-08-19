package kr.co.goldenhome.implement;

import kr.co.goldenhome.DailyDietImageApi;
import kr.co.goldenhome.DailyDietImageApiResponse;
import kr.co.goldenhome.dto.DailyDietImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyDietImageReader {

    private final DailyDietImageApi dailyDietImageApi;

    public DailyDietImageResponse getLatest(Long dailyDietId) {
        DailyDietImageApiResponse apiResponse = dailyDietImageApi.getLatest(dailyDietId);
        if (apiResponse == null) return DailyDietImageResponse.empty();
        return new DailyDietImageResponse(apiResponse.id(), apiResponse.dailyDietType(), apiResponse.formattedName(), apiResponse.imageUrl(), apiResponse.createdAt());
    }

    public List<DailyDietImageResponse> get(Long dailyDietId) {
        return dailyDietImageApi.get(dailyDietId)
                .stream().map(apiResponse -> new DailyDietImageResponse(apiResponse.id(), apiResponse.dailyDietType(), apiResponse.formattedName(), apiResponse.imageUrl(), apiResponse.createdAt())).toList();
    }
}
