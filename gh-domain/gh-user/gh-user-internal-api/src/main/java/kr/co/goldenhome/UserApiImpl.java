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

    /**
     * UserRepository 를 직접쓰지깐 커스텀에러를 못가져와서 공통모듈의존해야함
     */
    @Override
    public String getLoginId(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND, "UserApiImpl.getUsername")).getLoginId();
    }
}
