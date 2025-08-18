package kr.co.goldenhome;

public record ImageInfo(
        String dailyDietType,
        String formattedImageName
) {

    public static ImageInfo create(String dailyDietType, String formattedImageName) {
        return new ImageInfo(dailyDietType, formattedImageName);
    }
}
