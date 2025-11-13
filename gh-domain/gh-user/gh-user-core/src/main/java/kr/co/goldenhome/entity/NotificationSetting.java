package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.enums.NotificationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "notification_settings")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private Boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private NotificationSetting(Long id, Long userId, NotificationType notificationType, Boolean isEnabled, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.notificationType = notificationType;
        this.isEnabled = isEnabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static NotificationSetting create(Long userId, NotificationType notificationType, Boolean isEnabled) {
        return NotificationSetting.builder()
                .userId(userId)
                .notificationType(notificationType)
                .isEnabled(isEnabled)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void updateStatus(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}
