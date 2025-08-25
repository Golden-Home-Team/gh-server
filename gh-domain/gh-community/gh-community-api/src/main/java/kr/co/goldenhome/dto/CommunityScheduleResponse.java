package kr.co.goldenhome.dto;

import kr.co.goldenhome.entity.CommunitySchedule;

import java.time.LocalDate;

public record CommunityScheduleResponse(
        Long id,
        LocalDate recordDate,
        String content
) {

    public static CommunityScheduleResponse from(CommunitySchedule schedule) {
        return new CommunityScheduleResponse(schedule.getId(), schedule.getRecordDate(), schedule.getContent());
    }
}
