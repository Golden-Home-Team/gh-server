package kr.co.goldenhome.account.service;

import kr.co.goldenhome.account.dto.NotifySetting;
import kr.co.goldenhome.authentication.dto.FcmRequest;
import kr.co.goldenhome.entity.UserFcmToken;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.infrastructure.TokenRepository;
import kr.co.goldenhome.infrastructure.UserFcmTokenRepository;
import kr.co.goldenhome.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserFcmTokenRepository userFcmTokenRepository;

    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "UserAuthenticationManager.withdraw"));
        user.withdraw();
    }

    public void logout(Long userId) {
        tokenRepository.deleteByKey(String.valueOf(userId));
    }

    @Transactional
    public void saveOrUpdateFcmToken(FcmRequest request, Long userId) {
        userFcmTokenRepository.findByUserIdOrToken(userId, request.fcmToken()).ifPresentOrElse(
                UserFcmToken::renewUpdatedAt,
                () -> {
                    UserFcmToken userFcmToken = UserFcmToken.create(userId, request.fcmToken(), request.deviceId());
                    userFcmTokenRepository.save(userFcmToken);
                }
        );
    }

    @Transactional
    public void setNotification(NotifySetting notifySetting, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "UserAuthenticationManager.withdraw"));
        user.setNotification(notifySetting.notice(), notifySetting.chat());
    }
}
