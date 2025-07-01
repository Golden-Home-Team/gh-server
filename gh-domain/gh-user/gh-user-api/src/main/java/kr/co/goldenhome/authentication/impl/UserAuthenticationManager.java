package kr.co.goldenhome.authentication.impl;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.infrastructure.PasswordProcessor;
import kr.co.goldenhome.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthenticationManager {

    private final UserRepository userRepository;
    private final PasswordProcessor passwordProcessor;

    public User authenticate(String loginId, String password) {
        User user = userRepository.findByLoginId(loginId).orElse(null);
        if (user == null || !passwordProcessor.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED, "AuthenticationManager.authenticate");
        }
        return user;
    }
}
