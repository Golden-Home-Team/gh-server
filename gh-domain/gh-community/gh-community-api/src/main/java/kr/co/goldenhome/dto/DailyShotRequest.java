package kr.co.goldenhome.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record DailyShotRequest(String content,
                               @NotNull LocalDate recordDate,
                               List<DailyShotImageInfoRequest> dailyShotImageInfoRequests) {

}
