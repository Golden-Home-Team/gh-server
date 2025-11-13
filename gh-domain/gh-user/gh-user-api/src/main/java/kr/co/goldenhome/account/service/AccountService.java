package kr.co.goldenhome.account.service;

import kr.co.goldenhome.account.dto.NotificationSettingRequest;
import kr.co.goldenhome.authentication.dto.FcmRequest;
import kr.co.goldenhome.entity.NotificationSetting;
import kr.co.goldenhome.entity.UserFcmToken;
import kr.co.goldenhome.enums.NotificationType;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.infrastructure.NotificationSettingRepository;
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
    private final NotificationSettingRepository notificationSettingRepository;

    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "AccountService.withdraw"));
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
    public void updateNotificationSetting(NotificationSettingRequest request, Long userId) {
        notificationSettingRepository.findByUserIdAndNotificationType(userId, NotificationType.valueOf(request.type())).ifPresentOrElse(
                notificationSetting -> notificationSetting.updateStatus(request.isEnabled()),
                () -> {
                    NotificationSetting notificationSetting = NotificationSetting.create(userId, NotificationType.valueOf(request.type()), request.isEnabled());
                    notificationSettingRepository.save(notificationSetting);
                }
        );
    }
}
