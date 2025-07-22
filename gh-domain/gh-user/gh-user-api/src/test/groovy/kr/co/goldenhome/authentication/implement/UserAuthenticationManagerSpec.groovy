package kr.co.goldenhome.authentication.implement

import exception.CustomException
import exception.ErrorCode
import kr.co.goldenhome.SocialLoginException
import kr.co.goldenhome.SocialLoginManager
import kr.co.goldenhome.SocialPlatform
import kr.co.goldenhome.UserInfo
import kr.co.goldenhome.entity.User
import kr.co.goldenhome.enums.ProviderType
import kr.co.goldenhome.infrastructure.PasswordProcessor
import kr.co.goldenhome.infrastructure.UserRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Unroll


class UserAuthenticationManagerSpec extends Specification {

    UserAuthenticationManager userAuthenticationManager
    UserRepository userRepository = Mock()
    PasswordProcessor passwordProcessor = Mock()
    SocialLoginManager socialLoginManager = Mock()

    def setup() {
        userAuthenticationManager = new UserAuthenticationManager(userRepository, passwordProcessor,socialLoginManager)
    }

    def "authenticate - UserRepository.findByLoginId, PasswordProcessor.matches 를 호출한다" () {
        given:
        def givenLoginId = "gucoding1234"
        def givenPassword = "1234"
        def expectedUser = User.builder().password("1234").build()

        when:
        userAuthenticationManager.authenticate(givenLoginId, givenPassword)

        then:
        1 * userRepository.findByLoginId(givenLoginId) >> {
            String loginId ->
                loginId == givenLoginId
                Optional.of(expectedUser)
        }

        and:
        1 * passwordProcessor.matches(givenPassword, expectedUser.password) >> {
            String rawPassword, String encodedPassword ->
                rawPassword == givenPassword
                encodedPassword == expectedUser.password
                true
        }
    }

    @Unroll
    def "authenticate - #description"() {
        given:
        1 * userRepository.findByLoginId(*_) >> findByLoginIdResult
        (findByLoginIdResult.isPresent() ? 1 : 0) * passwordProcessor.matches(*_) >> matchesResult

        when:
        userAuthenticationManager.authenticate("gucoding@navercom", "1233")

        then:
        def exception = thrown(CustomException)
        exception.getErrorCode() == expectedErrorCode

        where:
        description       | findByLoginIdResult            | matchesResult | expectedErrorCode
        "존재하지 않는 사용자" | Optional.empty()             | false         | ErrorCode.LOGIN_FAILED
        "비밀번호 불일치"     |  Optional.of(User.builder().build()) | false         | ErrorCode.LOGIN_FAILED
    }

    def "getAuthorizationCode - SocialLoginManager 을 호출한다."() {
        given:
        def givenSocialPlatform = SocialPlatform.KAKAO

        when:
        userAuthenticationManager.getAuthorizationCode(givenSocialPlatform)

        then:
        1 * socialLoginManager.getAuthorizationCode(*_) >> {
            SocialPlatform socialPlatform ->
                socialPlatform == givenSocialPlatform
                ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "url").build()
        }
    }

    def "getUserInfo - SocialLoginManager, UserRepository 를 호출하며, 기존 유저가 존재할 때 해당 유저를 반환한다"() {
        given:
        def givenSocialPlatform = SocialPlatform.KAKAO
        def givenAuthorizationCode = "1234-a"
        def givenUserInfo = new UserInfo("kakao_123", "카카오유저")
        def existingUser = User.socialLogin(ProviderType.KAKAO, "kakao_123", "카카오유저")

        when:
        def resultUser = userAuthenticationManager.getUserInfo(givenSocialPlatform, givenAuthorizationCode)

        then:
        1 * socialLoginManager.getUserInfo(givenSocialPlatform, givenAuthorizationCode) >> givenUserInfo
        1 * userRepository.findByProviderTypeAndProviderId(ProviderType.KAKAO, "kakao_123") >> Optional.of(existingUser)
        0 * userRepository.save(*_)

        resultUser == existingUser
    }

    def "getUserInfo - SocialLoginManager, UserRepository를 호출하며, 기존 유저가 없을 때 새로운 유저를 저장하고 반환한다"() {
        given:
        def givenSocialPlatform = SocialPlatform.KAKAO
        def givenAuthorizationCode = "1234-a"
        def givenUserInfo = new UserInfo("kakao_123", "새로운카카오유저")
        def savedUser = User.socialLogin(ProviderType.KAKAO, "kakao_123", "새로운카카오유저")

        when:
        def resultUser = userAuthenticationManager.getUserInfo(givenSocialPlatform, givenAuthorizationCode)

        then:
        1 * socialLoginManager.getUserInfo(givenSocialPlatform, givenAuthorizationCode) >> givenUserInfo
        1 * userRepository.findByProviderTypeAndProviderId(ProviderType.KAKAO, "kakao_123") >> Optional.empty()
        1 * userRepository.save({ User user ->
            user.providerType == ProviderType.KAKAO &&
                    user.providerId == "kakao_123" &&
                    user.username == "새로운카카오유저"
        }) >> savedUser

        resultUser == savedUser
    }

    def "getUserInfo - SocialLoginException 발생 시 CustomException 으로 변환하여 던진다"() {
        given:
        def givenSocialPlatform = SocialPlatform.KAKAO
        def givenAuthorizationCode = "1234-a"

        when:
        userAuthenticationManager.getUserInfo(givenSocialPlatform, givenAuthorizationCode)

        then:
        1 * socialLoginManager.getUserInfo(givenSocialPlatform, givenAuthorizationCode) >> { throw new SocialLoginException() }
        0 * userRepository._

        def e = thrown(CustomException)
        e.getErrorCode() == ErrorCode.SOCIAL_LOGIN_FAILED
        e.getOrigin() == "UserAuthenticationManager.getUserInfo"
    }

}
