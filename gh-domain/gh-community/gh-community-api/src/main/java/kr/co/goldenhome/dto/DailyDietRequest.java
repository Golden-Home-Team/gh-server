package kr.co.goldenhome.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record DailyDietRequest(String content,
                               @NotNull LocalDate recordDate,
                               List<DailyDietImageInfoRequest> dailyDietImageInfoRequests) {

}
