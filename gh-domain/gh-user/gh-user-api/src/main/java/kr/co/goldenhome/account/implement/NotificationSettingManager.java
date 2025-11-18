package kr.co.goldenhome.account.implement;

import jakarta.validation.constraints.NotNull;
import kr.co.goldenhome.entity.NotificationSetting;
import kr.co.goldenhome.enums.NotificationType;
import kr.co.goldenhome.infrastructure.NotificationSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationSettingManager {

    private final NotificationSettingRepository notificationSettingRepository;

    public void update(NotificationType notificationType, Boolean isEnabled, Long userId) {
        notificationSettingRepository.findByUserIdAndNotificationType(userId, notificationType).ifPresentOrElse(
                notificationSetting -> notificationSetting.updateStatus(isEnabled),
                () -> {
                    NotificationSetting notificationSetting = NotificationSetting.create(userId, notificationType, isEnabled);
                    notificationSettingRepository.save(notificationSetting);
                }
        );
    }
}
