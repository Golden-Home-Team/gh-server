package kr.co.goldenhome.profile.service;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.ProfileImageApi;
import kr.co.goldenhome.ProfileImageApiResponse;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.infrastructure.UserRepository;
import kr.co.goldenhome.profile.dto.ProfileImageRequest;
import kr.co.goldenhome.profile.dto.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final ProfileImageApi profileImageApi;

    public ProfileResponse get(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "ProfileService.get"));
        ProfileImageApiResponse profileImageApiResponse = profileImageApi.getByUserId(userId);
        return new ProfileResponse(profileImageApiResponse, user.getUsername(), user.getLoginId(), user.getPhoneNumber(), user.getEmail());
    }

    public void createProfileImage(ProfileImageRequest request, Long userId) {
        profileImageApi.save(request.formattedImageName(), userId);
    }
}
