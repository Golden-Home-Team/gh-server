package kr.co.goldenhome.infrastructure;

import kr.co.goldenhome.entity.UserFcmToken;
import kr.co.goldenhome.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserFcmTokenRepository extends JpaRepository<UserFcmToken, Long> {
    Optional<UserFcmToken> findByUserIdAndToken(Long userId, String token);

    @Query(
            value = "select distinct uft.id, uft.user_id, uft.token, uft.device_id, uft.updated_at " +
                    "from user_fcm_tokens uft " +
                    "join notification_settings on uft.user_id = notification_settings.user_id " +
                    "where uft.user_id = :userId and notification_settings.notification_type = :type " +
                    "and notification_settings.is_enabled = true"
            ,nativeQuery = true
    )
    List<UserFcmToken> findEnabledTokenByType(@Param("userId") Long userId, @Param("type") String type);

    @Query(
            value = "select distinct uft.id, uft.user_id, uft.token, uft.device_id, uft.updated_at " +
                    "from user_fcm_tokens uft " +
                    "join notification_settings ns on uft.user_id = ns.user_id " +
                    "where uft.user_id in :userIds and ns.notification_type = :type " +
                    "and ns.is_enabled = true"
            , nativeQuery = true
    )
    List<UserFcmToken> findEnabledTokensByUserIdsAndType(
            @Param("userIds") List<Long> userIds,
            @Param("type") String type
    );
}
