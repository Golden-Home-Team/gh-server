package kr.co.goldenhome.profile.service;

import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.ProfileImageApi;
import kr.co.goldenhome.ProfileImageApiResponse;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.infrastructure.PasswordProcessor;
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
    private final PasswordProcessor passwordProcessor;

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

    @Transactional
    public void modifyPhoneNumber(ProfilePhoneNumberRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "ProfileService.modifyPhoneNumber"));
        user.modifyPhoneNumber(request.phoneNumber());
    }

    @Transactional
    public void modifyEmail(ProfileEmailRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "ProfileService.modifyEmail"));
        signupManager.isEmailDuplicated(request.email());
        user.modifyEmail(request.email());
    }

    @Transactional
    public void modifyPassword(ProfilePasswordRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "ProfileService.modifyEmail"));
        String encodePassword = passwordProcessor.encode(request.password());
        user.modifyPassword(encodePassword);
    }
}
