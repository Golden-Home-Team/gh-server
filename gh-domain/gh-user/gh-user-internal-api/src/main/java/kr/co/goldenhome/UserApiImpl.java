package kr.co.goldenhome;

import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserApiImpl implements UserApi {

    private final UserRepository userRepository;

    @Override
    public String getLoginId(Long userId) {
        return userRepository.findById(userId).map(User::getLoginId).orElse(null);
    }

    @Override
    public String getUserName(Long userId) {
        return userRepository.findById(userId).map(User::getUsername).orElse(null);
    }
}
