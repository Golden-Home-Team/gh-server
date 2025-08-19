package kr.co.goldenhome;

public record DailyShotImageInfo(
        String dailyShotType,
        String formattedImageName
) {

    public static DailyShotImageInfo create(String dailyShotType, String formattedImageName) {
        return new DailyShotImageInfo(dailyShotType, formattedImageName);
    }
}
