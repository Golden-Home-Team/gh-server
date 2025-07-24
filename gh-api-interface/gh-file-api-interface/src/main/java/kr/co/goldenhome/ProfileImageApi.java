package kr.co.goldenhome;

public interface ProfileImageApi {
    ProfileImageApiResponse getByUserId(Long userId);

    void save(String formattedImageName, Long userId);
}
