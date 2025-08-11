package kr.co.goldenhome;

public record ReviewMetaData(double averageScore,
                             int totalCount,
                             int onePointCount,
                             int twoPointCount,
                             int threePointCount,
                             int fourPointCount,
                             int fivePointCount) {

    public static ReviewMetaData noData() {
        return new ReviewMetaData(0, 0, 0, 0, 0, 0, 0);
    }
}
