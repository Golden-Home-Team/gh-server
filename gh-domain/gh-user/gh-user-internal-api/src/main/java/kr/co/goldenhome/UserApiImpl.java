package kr.co.goldenhome;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserApiImpl implements UserApi {

    private final UserRepository userRepository;

    @Override
    public String getLoginId(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND, "UserApiImpl.getUsername")).getLoginId();
    }

    @Override
    public String getUserName(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND, "UserApiImpl.getUsername")).getUsername();
    }
}
