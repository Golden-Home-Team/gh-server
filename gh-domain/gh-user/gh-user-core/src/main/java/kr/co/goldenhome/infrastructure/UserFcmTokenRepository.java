package kr.co.goldenhome.infrastructure;

import kr.co.goldenhome.entity.UserFcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserFcmTokenRepository extends JpaRepository<UserFcmToken, Long> {
    Optional<UserFcmToken> findByUserIdAndToken(Long userId, String token);
    /**
     *
     * 1대1 채팅방일 경우에 사용,
     * 다수채팅방 요구사항에는 비효율적이라 추가적인 쿼리를 짜야한다. (여러개의 fcmToken 중 updatedAt이 최신 기기에만 알림 전송 하는 등)
     */
    List<UserFcmToken> findAllByUserIdIn(List<Long> userIds);

    @Query(
            value = "select uft.id, uft.user_id, uft.token, uft.device_id, uft.updated_at " +
                    "from user_fcm_tokens uft " +
                    "join notification_settings on uft.user_id = notification_settings.user_id " +
                    "where uft.user_id = :userId and notification_settings.is_enabled = true"
            ,nativeQuery = true
    )
    List<UserFcmToken> findEnabledToken(Long userId);
}
