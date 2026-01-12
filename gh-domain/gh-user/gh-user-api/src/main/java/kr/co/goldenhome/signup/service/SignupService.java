package kr.co.goldenhome.signup.service;

import kr.co.goldenhome.authentication.implement.VerificationManagerFactory;
import kr.co.goldenhome.signup.dto.SignupRequest;
import kr.co.goldenhome.signup.implement.SignupManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final SignupManager signupManager;
    private final VerificationManagerFactory verificationManagerFactory;

    public void isLoginIdDuplicated(String loginId) {
        signupManager.isLoginIdDuplicated(loginId);
    }

    public void signup(SignupRequest request) {
        verificationManagerFactory.confirm(request.type(), request.contact(), request.verificationCode());
        signupManager.createUser(request);
    }

    public void isEmailDuplicated(String email) {
        signupManager.isEmailDuplicated(email);
    }
}
