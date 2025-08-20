package kr.co.goldenhome.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record DailyShotUpdateRequest(String content,
                                     List<DailyShotImageInfoRequest> dailyShotImageInfoRequests) {

}
