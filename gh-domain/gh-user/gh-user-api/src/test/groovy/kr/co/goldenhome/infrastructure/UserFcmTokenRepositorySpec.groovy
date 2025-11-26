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

        def targetChatSettingEnabledTrue = NotificationSetting.builder()
                .userId(targetUserId)
                .isEnabled(true)
                .notificationType(NotificationType.CHAT)
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


        notificationSettingRepository.saveAllAndFlush(List.of(targetSettingEnabledTrue, otherSettingEnabledFalse, targetChatSettingEnabledTrue))
        userFcmTokenRepository.saveAllAndFlush(List.of(targetToken1, targetToken2, otherToken))

    }

    def "findEnabledTokenByType 은 is_enabled 가 true 인 사용자의 Fcm 토큰을 조회해야 한다"() {

        when: "findEnabledToken 메서드를 호출할 때"
        List<UserFcmToken> result = userFcmTokenRepository.findEnabledTokenByType(100L, NotificationType.NOTICE.name())
        for (UserFcmToken u : result) {
            println "u.id = $u.id"
        }

        then: "is_enabled 가 true 인 타겟 유저의 모든 토큰 목록이 반환되어야 한다"
        result.size() == 2

        def fcmTokens = result.collect { it.token }
        fcmTokens.contains("enabled_token_for_user_100")
        fcmTokens.contains("disabled_token_for_user_100")
    }

    def "findEnabledTokenByType 은 활성화된 설정이 없는 사용자에 대해 빈 리스트를 반환해야 한다"() {

        when: "findEnabledToken 메서드를 호출할 때"
        List<UserFcmToken> result = userFcmTokenRepository.findEnabledTokenByType(200, NotificationType.NOTICE.name())

        then: "결과는 빈 리스트여야 한다"
        result.isEmpty()
    }

    def "findEnabledTokenByType 은 타입별 설정에 따라 올바른 토큰만 반환해야 한다"() {
        given:
        Long userId = 300L

        def t1 = UserFcmToken.builder()
                .userId(userId)
                .token("user300_token1")
                .updatedAt(LocalDateTime.now())
                .build()

        def noticeFalse = NotificationSetting.builder()
                .userId(userId)
                .notificationType(NotificationType.NOTICE)
                .isEnabled(false)
                .build()

        def chatTrue = NotificationSetting.builder()
                .userId(userId)
                .notificationType(NotificationType.CHAT)
                .isEnabled(true)
                .build()

        notificationSettingRepository.saveAllAndFlush([noticeFalse, chatTrue])
        userFcmTokenRepository.saveAllAndFlush([t1])

        when:
        def noticeResult = userFcmTokenRepository.findEnabledTokenByType(userId, NotificationType.NOTICE.name())
        def chatResult = userFcmTokenRepository.findEnabledTokenByType(userId, NotificationType.CHAT.name())

        then:
        noticeResult.isEmpty()
        chatResult.size() == 1
        chatResult*.token.contains("user300_token1")
    }

    def "findEnabledTokenByType 은 여러 NotificationType 이 존재해도 요청한 타입만 필터링해야 한다"() {
        given:
        Long userId = 400L

        def t1 = UserFcmToken.builder()
                .userId(userId)
                .token("user400_token1")
                .updatedAt(LocalDateTime.now())
                .build()

        def noticeSetting = NotificationSetting.builder()
                .userId(userId)
                .notificationType(NotificationType.NOTICE)
                .isEnabled(true)
                .build()

        def chatSetting = NotificationSetting.builder()
                .userId(userId)
                .notificationType(NotificationType.CHAT)
                .isEnabled(false)
                .build()

        notificationSettingRepository.saveAllAndFlush([noticeSetting, chatSetting])
        userFcmTokenRepository.saveAllAndFlush([t1])

        when:
        def noticeResult = userFcmTokenRepository.findEnabledTokenByType(userId, NotificationType.NOTICE.name())
        def chatResult = userFcmTokenRepository.findEnabledTokenByType(userId, NotificationType.CHAT.name())

        then:
        noticeResult.size() == 1
        chatResult.isEmpty()
    }

    def "findEnabledTokenByType 은 동일한 타입 설정이 여러 개여도 FCM 토큰이 중복되지 않아야 한다"() {
        given:
        Long userId = 500L

        def t1 = UserFcmToken.builder()
                .userId(userId)
                .token("user500_token1")
                .updatedAt(LocalDateTime.now())
                .build()

        def setting1 = NotificationSetting.builder()
                .userId(userId)
                .notificationType(NotificationType.NOTICE)
                .isEnabled(true)
                .build()

        def setting2 = NotificationSetting.builder()
                .userId(userId)
                .notificationType(NotificationType.NOTICE)
                .isEnabled(true)
                .build()

        notificationSettingRepository.saveAllAndFlush([setting1, setting2])
        userFcmTokenRepository.saveAndFlush(t1)

        when:
        def result = userFcmTokenRepository.findEnabledTokenByType(userId, NotificationType.NOTICE.name())

        then:
        result.size() == 1
        result[0].token == "user500_token1"
    }

    def "findEnabledTokenByType 은 해당 타입의 NotificationSetting 이 없으면 빈 리스트를 반환해야 한다"() {
        given:
        Long userId = 600L

        def t1 = UserFcmToken.builder()
                .userId(userId)
                .token("user600_token1")
                .updatedAt(LocalDateTime.now())
                .build()

        userFcmTokenRepository.saveAndFlush(t1)

        when:
        def result = userFcmTokenRepository.findEnabledTokenByType(userId, NotificationType.NOTICE.name())

        then:
        result.isEmpty()
    }

    def "findEnabledTokensByUserIdsAndType 은 주어진 userIds 중 is_enabled=true 인 사용자만 조회해야 한다"() {

        given: "두 사용자 ID 리스트"
        List<Long> userIds = [100L, 200L]

        when: "NOTICE 타입의 활성 사용자 토큰을 조회하면"
        def result = userFcmTokenRepository.findEnabledTokensByUserIdsAndType(
                userIds,
                NotificationType.NOTICE.name()
        )

        then: "is_enabled=true 인 userId=100 의 토큰들만 반환되어야 한다"
        result.size() == 2

        and: "반환된 토큰 값 검증"
        def tokens = result*.token
        tokens.containsAll([
                "enabled_token_for_user_100",
                "disabled_token_for_user_100"
        ])

        and: "userId=200 의 토큰은 포함되지 않아야 한다"
        !tokens.contains("enabled_token_for_user_200")
    }







}
