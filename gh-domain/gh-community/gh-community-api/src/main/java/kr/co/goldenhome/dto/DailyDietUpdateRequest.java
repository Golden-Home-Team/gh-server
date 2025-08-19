package kr.co.goldenhome.dto;



import java.util.List;

public record DailyDietUpdateRequest(String content,
                                     List<DailyDietImageInfoRequest> dailyDietImageInfoRequests) {
}
