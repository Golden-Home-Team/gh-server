package kr.co.goldenhome.signup.implement;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.enums.UserRole;
import kr.co.goldenhome.infrastructure.PasswordProcessor;
import kr.co.goldenhome.infrastructure.UserRepository;
import kr.co.goldenhome.signup.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignupManager {

    private final UserRepository userRepository;
    private final PasswordProcessor passwordProcessor;

    public void isLoginIdDuplicated(String loginId) {
        boolean userExists = userRepository.existsByLoginId(loginId);
        if (userExists) throw new CustomException(ErrorCode.DUPLICATED_LOGIN_ID, "SignupManager.isLoginIdDuplicated");
    }

    public void createUser(SignupRequest request) {
        String encodedPassword = passwordProcessor.encode(request.password());
        userRepository.save(User.create(request.loginId(), request.email(), encodedPassword, request.phoneNumber(), UserRole.USER));
    }

}
