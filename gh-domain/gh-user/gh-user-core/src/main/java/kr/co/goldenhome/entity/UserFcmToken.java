package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "user_fcm_tokens")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @Column(unique = true)
    private String token;
    private String deviceId;
    private LocalDateTime updatedAt;

    @Builder
    private UserFcmToken(Long id, Long userId, String token, String deviceId, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.deviceId = deviceId;
        this.updatedAt = updatedAt;
    }

    public static UserFcmToken create(Long userId, String token, String deviceId) {
        return UserFcmToken.builder()
                .userId(userId)
                .token(token)
                .deviceId(deviceId)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void renewUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
