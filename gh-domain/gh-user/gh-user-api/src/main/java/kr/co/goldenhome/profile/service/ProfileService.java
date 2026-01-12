package kr.co.goldenhome.profile.service;

import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.ProfileImageApi;
import kr.co.goldenhome.ProfileImageApiResponse;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.infrastructure.UserRepository;
import kr.co.goldenhome.profile.dto.*;
import kr.co.goldenhome.signup.implement.SignupManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final ProfileImageApi profileImageApi;
    private final SignupManager signupManager;

    public ProfileResponse get(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "ProfileService.get"));
        ProfileImageApiResponse profileImageApiResponse = profileImageApi.getByUserId(userId);
        return new ProfileResponse(profileImageApiResponse, user.getUsername(), user.getLoginId(), user.getPhoneNumber(), user.getEmail());
    }

    public void createProfileImage(ProfileImageRequest request, Long userId) {
        profileImageApi.save(request.formattedImageName(), userId);
    }

    @Transactional
    public void modifyName(ProfileNameRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "ProfileService.modifyName"));
        user.modifyName(request.name());
    }

    @Transactional
    public void modifyLoginId(ProfileLoginIdRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "ProfileService.modifyLoginId"));
        signupManager.isLoginIdDuplicated(request.loginId());
        user.modifyLoginId(request.loginId());
    }

}
