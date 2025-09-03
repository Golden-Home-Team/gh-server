package kr.co.goldenhome.account.service;

import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.infrastructure.TokenRepository;
import kr.co.goldenhome.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "UserAuthenticationManager.withdraw"));
        user.withdraw();
    }

    public void logout(Long userId) {
        tokenRepository.deleteByKey(String.valueOf(userId));
    }
}
