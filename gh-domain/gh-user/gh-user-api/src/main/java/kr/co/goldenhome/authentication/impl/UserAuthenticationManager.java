package kr.co.goldenhome.authentication.impl;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.SocialLoginException;
import kr.co.goldenhome.SocialLoginManager;
import kr.co.goldenhome.SocialPlatform;
import kr.co.goldenhome.UserInfo;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.enums.ProviderType;
import kr.co.goldenhome.infrastructure.PasswordProcessor;
import kr.co.goldenhome.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAuthenticationManager {

    private final UserRepository userRepository;
    private final PasswordProcessor passwordProcessor;
    private final SocialLoginManager socialLoginManager;

    public User authenticate(String loginId, String password) {
        User user = userRepository.findByLoginId(loginId).orElse(null);
        if (user == null || !passwordProcessor.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED, "AuthenticationManager.authenticate");
        }
        return user;
    }

    public ResponseEntity<Void> getAuthorizationCode(SocialPlatform socialPlatform) {
        return socialLoginManager.getAuthorizationCode(socialPlatform);
    }

    public User getUserInfo(SocialPlatform socialPlatform, String authorizationCode) {
        try {
            UserInfo userInfo = socialLoginManager.getUserInfo(socialPlatform, authorizationCode);
            ProviderType providerType = ProviderType.valueOf(socialPlatform.name());
            Optional<User> socialUser = userRepository.findByProviderTypeAndProviderId(providerType, userInfo.providerId());
            return socialUser.orElseGet(() -> userRepository.save(User.socialLogin(providerType, userInfo.providerId(), userInfo.username())));
        } catch (SocialLoginException e) {
            throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED, "UserAuthenticationManager.getUserInfo");
        }
    }
}
