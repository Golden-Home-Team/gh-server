package kr.co.goldenhome.dto;

import java.time.LocalDate;
import java.util.List;

public record DailyShotResponse(Long dailyShotId,
                                String content,
                                LocalDate recordDate,
                                List<DailyShotImageResponse> dailyShotImageResponses) {
}
