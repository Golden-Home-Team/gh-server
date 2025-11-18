package kr.co.goldenhome.infrastructure;

import kr.co.goldenhome.entity.NotificationSetting;
import kr.co.goldenhome.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {
    Optional<NotificationSetting> findByUserIdAndNotificationType(Long userId, NotificationType notificationType);
}
