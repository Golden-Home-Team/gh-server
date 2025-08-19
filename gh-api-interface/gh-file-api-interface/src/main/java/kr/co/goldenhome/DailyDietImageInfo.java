package kr.co.goldenhome;

public record DailyDietImageInfo(
        String dailyDietType,
        String formattedImageName
) {

    public static DailyDietImageInfo create(String dailyDietType, String formattedImageName) {
        return new DailyDietImageInfo(dailyDietType, formattedImageName);
    }
}
