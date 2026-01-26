package kr.co.goldenhome.account.service;

import kr.co.goldenhome.FcmManager;
import kr.co.goldenhome.account.dto.NotificationSettingRequest;
import kr.co.goldenhome.account.dto.WithdrawRequest;
import kr.co.goldenhome.account.implement.NotificationSettingManager;
import kr.co.goldenhome.authentication.dto.FcmRequest;
import kr.co.goldenhome.entity.UserFcmToken;
import kr.co.goldenhome.enums.NotificationType;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.infrastructure.TokenRepository;
import kr.co.goldenhome.infrastructure.UserFcmTokenRepository;
import kr.co.goldenhome.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserFcmTokenRepository userFcmTokenRepository;
    private final NotificationSettingManager notificationSettingManager;
    private final FcmManager fcmManager;

    @Transactional
    public void withdraw(WithdrawRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "AccountService.withdraw"));
        user.withdraw(request.reason());
    }

    public void logout(Long userId) {
        tokenRepository.deleteByKey(String.valueOf(userId));
    }

    @Transactional
    public void saveOrUpdateFcmToken(FcmRequest request, Long userId) {
        userFcmTokenRepository.findByUserIdAndToken(userId, request.fcmToken()).ifPresentOrElse(
                UserFcmToken::renewUpdatedAt,
                () -> {
                    UserFcmToken userFcmToken = UserFcmToken.create(userId, request.fcmToken(), request.deviceId());
                    userFcmTokenRepository.save(userFcmToken);
                }
        );
    }

    @Transactional
    public void updateNotificationSetting(NotificationSettingRequest request, Long userId) {
        NotificationType notificationType = NotificationType.valueOf(request.type());
        notificationSettingManager.update(notificationType, request.isEnabled(), userId);
        List<String> fcmTokens = userFcmTokenRepository.findEnabledTokenByType(userId, NotificationType.NOTICE.name()).stream().map(UserFcmToken::getToken).toList();
        fcmManager.subscribeToTopic(fcmTokens, notificationType.name());
    }
}
