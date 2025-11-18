package kr.co.goldenhome.infrastructure

import jakarta.persistence.EntityManager
import kr.co.goldenhome.entity.NotificationSetting
import kr.co.goldenhome.entity.UserFcmToken
import kr.co.goldenhome.enums.NotificationType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.time.LocalDateTime

@ActiveProfiles("test")
@DataJpaTest
class UserFcmTokenRepositorySpec extends Specification {

    @Autowired
    UserFcmTokenRepository userFcmTokenRepository

    @Autowired
    NotificationSettingRepository notificationSettingRepository

    @Autowired
    EntityManager entityManager

    def setup() {
        Long targetUserId = 100L
        Long otherUserId = 200L

        def targetToken1 = UserFcmToken.builder()
                .userId(targetUserId)
                .token("enabled_token_for_user_100")
                .updatedAt(LocalDateTime.now())
                .build()

        def targetToken2 = UserFcmToken.builder()
                .userId(targetUserId)
                .token("disabled_token_for_user_100")
                .updatedAt(LocalDateTime.now().minusHours(1))
                .build()

        def targetSettingEnabledTrue = NotificationSetting.builder()
                .userId(targetUserId)
                .isEnabled(true)
                .notificationType(NotificationType.NOTICE)
                .build()

        def otherToken = UserFcmToken.builder()
                .userId(otherUserId)
                .token("enabled_token_for_user_200")
                .updatedAt(LocalDateTime.now())
                .build()

        def otherSettingEnabledFalse = NotificationSetting.builder()
                .userId(otherUserId)
                .isEnabled(false)
                .notificationType(NotificationType.NOTICE)
                .build()

        notificationSettingRepository.saveAllAndFlush(List.of(targetSettingEnabledTrue, otherSettingEnabledFalse))
        userFcmTokenRepository.saveAllAndFlush(List.of(targetToken1, targetToken2, otherToken))

    }

    def "findEnabledToken은 is_enabled가 true인 사용자의 Fcm 토큰을 조회해야 한다"() {

        when: "findEnabledToken 메서드를 호출할 때"
        List<UserFcmToken> result = userFcmTokenRepository.findEnabledToken(100L)
        for (UserFcmToken u : result) {
            println "u.id = $u.id"
        }

        then: "is_enabled가 true인 타겟 유저의 모든 토큰 목록이 반환되어야 한다"
        result.size() == 2

        def fcmTokens = result.collect { it.token }
        fcmTokens.contains("enabled_token_for_user_100")
        fcmTokens.contains("disabled_token_for_user_100")
    }

    def "findEnabledToken은 활성화된 설정이 없는 사용자에 대해 빈 리스트를 반환해야 한다"() {

        when: "findEnabledToken 메서드를 호출할 때"
        List<UserFcmToken> result = userFcmTokenRepository.findEnabledToken(200)

        then: "결과는 빈 리스트여야 한다"
        result.isEmpty()
    }
}
