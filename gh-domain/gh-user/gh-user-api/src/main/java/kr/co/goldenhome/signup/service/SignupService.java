package kr.co.goldenhome.signup.service;

import kr.co.goldenhome.dto.Signup;
import kr.co.goldenhome.entity.EmailVerification;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.enums.EmailVerificationType;

import kr.co.goldenhome.signup.implement.EmailVerificationManager;
import kr.co.goldenhome.signup.implement.SignupManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final SignupManager signupManager;
    private final EmailVerificationManager emailVerificationManager;

    public User signup(Signup signup) {
        return signupManager.createUser(signup);
    }

    public EmailVerification sendEmailForVerification(User user) {
        EmailVerification emailVerification = emailVerificationManager.create(user.getId(), user.getEmail(), EmailVerificationType.SIGN_UP);
        emailVerificationManager.sendEmail(emailVerification);
        return emailVerification;
    }

    @Transactional
    public void verify(String code) {
        Long userId = emailVerificationManager.verify(code);
        signupManager.active(userId);
    }
}
