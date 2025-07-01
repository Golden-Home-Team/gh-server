package kr.co.goldenhome.signup.service;

import kr.co.goldenhome.signup.dto.SignupRequest;
import kr.co.goldenhome.signup.implement.SignupManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final SignupManager signupManager;

    public void isLoginIdDuplicated(String loginId) {
        signupManager.isLoginIdDuplicated(loginId);
    }

    public void signup(SignupRequest request) {
        signupManager.createUser(request);
    }

}
