package kr.co.goldenhome;

import java.math.BigDecimal;

public record ReviewMetaData(BigDecimal averageScore,
                             Long totalCount,
                             Long onePointCount,
                             Long twoPointCount,
                             Long threePointCount,
                             Long fourPointCount,
                             Long fivePointCount) {

    public static ReviewMetaData noData() {
        return new ReviewMetaData(BigDecimal.ZERO, 0L, 0L, 0L, 0L, 0L, 0L);
    }
}
