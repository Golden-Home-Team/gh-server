package kr.co.goldenhome;

import org.springframework.stereotype.Component;

@Component
public class ProfileImageApiTestImpl implements ProfileImageApi {
    @Override
    public ProfileImageApiResponse getByUserId(Long userId) {
        return null;
    }

    @Override
    public void save(String formattedImageName, Long userId) {

    }
}
