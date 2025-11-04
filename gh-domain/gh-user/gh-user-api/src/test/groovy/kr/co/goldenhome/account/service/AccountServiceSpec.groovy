package kr.co.goldenhome.account.service

import kr.co.goldenhome.authentication.dto.FcmRequest
import kr.co.goldenhome.entity.User
import kr.co.goldenhome.entity.UserFcmToken
import kr.co.goldenhome.infrastructure.TokenRepository
import kr.co.goldenhome.infrastructure.UserFcmTokenRepository
import kr.co.goldenhome.infrastructure.UserRepository
import spock.lang.Specification

class AccountServiceSpec extends Specification {

    AccountService accountService
    UserRepository userRepository = Mock()
    TokenRepository tokenRepository = Mock()
    UserFcmTokenRepository userFcmTokenRepository = Mock()

    def setup() {
        accountService = new AccountService(userRepository,tokenRepository,userFcmTokenRepository)
    }

    def "withdraw - userRepository 를 호출한다"() {

        when:
        accountService.withdraw(1L)

        then:
        1 * userRepository.findById(*_) >> {
            Long userId ->
                userId == 1L
                Optional.of(User.builder().build())
        }
    }

    def "logout - tokenRepository 를 호출한다"() {

        when:
        accountService.logout(1L)

        then:
        1 * tokenRepository.deleteByKey("1")
    }

    def "saveOrUpdateFcmToken - userFcmTokenRepository 를 호출한다"() {
        given:
        def givenUserId = 1L
        def givenFcmToken = "adg1234"
        def givenDeviceId = "device-1"
        def givenRequest = new FcmRequest(givenFcmToken, givenDeviceId)
        def expectedResponse = UserFcmToken.create(givenUserId, givenFcmToken, givenDeviceId)

        when:
        accountService.saveOrUpdateFcmToken(givenRequest, givenUserId)

        then:
        1 * userFcmTokenRepository.findByUserIdOrToken(*_) >> {
            Long userId, String fcmToken ->
                userId == givenUserId
                fcmToken == givenFcmToken
                Optional.of(expectedResponse)
        }

    }

    def "saveOrUpdateFcmToken - userFcmTokenRepository 를 호출한다, present 라면 그대로 종료한다"() {
        given:
        def givenUserId = 1L
        def givenFcmToken = "adg1234"
        def givenDeviceId = "device-1"
        def givenRequest = new FcmRequest(givenFcmToken, givenDeviceId)

        when:
        accountService.saveOrUpdateFcmToken(givenRequest, givenUserId)

        then:
        1 * userFcmTokenRepository.findByUserIdOrToken(*_) >> {
            Long userId, String fcmToken ->
                userId == givenUserId
                fcmToken == givenFcmToken
                Optional.of(UserFcmToken.builder().build())
        }
        0 * userFcmTokenRepository.save(*_)

    }

    def "saveOrUpdateFcmToken - userFcmTokenRepository 를 호출한다, Not present 라면 save() 를 호출한다"() {
        given:
        def givenUserId = 1L
        def givenFcmToken = "adg1234"
        def givenDeviceId = "device-1"
        def givenRequest = new FcmRequest(givenFcmToken, givenDeviceId)

        when:
        accountService.saveOrUpdateFcmToken(givenRequest, givenUserId)

        then:
        1 * userFcmTokenRepository.findByUserIdOrToken(*_) >> {
            Long userId, String fcmToken ->
                userId == givenUserId
                fcmToken == givenFcmToken
                Optional.empty()
        }
        1 * userFcmTokenRepository.save(*_)

    }

}
