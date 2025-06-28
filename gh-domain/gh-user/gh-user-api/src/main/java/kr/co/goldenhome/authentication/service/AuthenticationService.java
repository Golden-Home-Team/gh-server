package kr.co.goldenhome.authentication.service;

import kr.co.goldenhome.authentication.dto.LoginRequest;
import kr.co.goldenhome.authentication.dto.LoginResponse;
import kr.co.goldenhome.authentication.impl.AuthenticationTokenManager;
import kr.co.goldenhome.authentication.impl.UserAuthenticationManager;
import kr.co.goldenhome.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserAuthenticationManager userAuthenticationManager;
    private final AuthenticationTokenManager authenticationTokenManager;

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userAuthenticationManager.authenticate(loginRequest.email(), loginRequest.password());
        return authenticationTokenManager.create(user.getId());
    }
}
