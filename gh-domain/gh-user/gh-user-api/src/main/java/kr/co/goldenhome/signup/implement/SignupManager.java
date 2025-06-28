package kr.co.goldenhome.signup.implement;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.dto.Signup;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.enums.UserRole;
import kr.co.goldenhome.infrastructure.PasswordProcessor;
import kr.co.goldenhome.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignupManager {

    private final UserRepository userRepository;
    private final PasswordProcessor passwordProcessor;

    public User createUser(Signup signup) {
        String encodedPassword = passwordProcessor.encode(signup.password());
        return userRepository.save(User.create(signup.username(), signup.email(), encodedPassword, UserRole.USER));
    }

    public void active(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NO_RESOURCE, "SignupManager.active"));
        user.active();
    }
}
