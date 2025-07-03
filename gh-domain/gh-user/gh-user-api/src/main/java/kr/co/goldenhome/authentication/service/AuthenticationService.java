package kr.co.goldenhome.authentication.service;

import jakarta.validation.constraints.NotBlank;
import kr.co.goldenhome.SocialPlatform;
import kr.co.goldenhome.authentication.dto.LoginRequest;
import kr.co.goldenhome.authentication.dto.LoginResponse;
import kr.co.goldenhome.authentication.impl.AuthenticationTokenManager;
import kr.co.goldenhome.authentication.impl.UserAuthenticationManager;
import kr.co.goldenhome.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserAuthenticationManager userAuthenticationManager;
    private final AuthenticationTokenManager authenticationTokenManager;

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userAuthenticationManager.authenticate(loginRequest.loginId(), loginRequest.password());
        return authenticationTokenManager.create(user.getId());
    }

    public ResponseEntity<Void> getAuthorizationCode(SocialPlatform socialPlatform) {
        return userAuthenticationManager.getAuthorizationCode(socialPlatform);
    }

    public LoginResponse getUserInfo(SocialPlatform socialPlatform, String authorizationCode) {
        User user = userAuthenticationManager.getUserInfo(socialPlatform, authorizationCode);
        return authenticationTokenManager.create(user.getId());
    }

    public LoginResponse refresh(String refreshToken) {
        return authenticationTokenManager.refresh(refreshToken);
    }
}
