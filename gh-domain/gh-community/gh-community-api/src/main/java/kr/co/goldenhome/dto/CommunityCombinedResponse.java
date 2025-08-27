package kr.co.goldenhome.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CommunityCombinedResponse(
        NoticeInfo noticeInfo,
        DailyDietInfo dailyDietInfo,
        DailyShotInfo dailyShotInfo,
        DailyMedicationInfo dailyMedicationInfo,
        DailyRehabilitationInfo dailyRehabilitationInfo,
        List<CommunityScheduleResponse> communityScheduleResponses,
        String communityManagerName
) {
}
