package kr.co.goldenhome.service;

import java.math.BigDecimal;

public record ReviewAppenderWriteResponse(
        BigDecimal averageScore
) {
}
