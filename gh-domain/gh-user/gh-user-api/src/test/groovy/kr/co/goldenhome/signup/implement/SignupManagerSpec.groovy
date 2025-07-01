package kr.co.goldenhome.signup.implement

import exception.CustomException
import exception.ErrorCode
import kr.co.goldenhome.entity.User
import kr.co.goldenhome.enums.UserRole
import kr.co.goldenhome.enums.UserStatus
import kr.co.goldenhome.infrastructure.PasswordProcessor
import kr.co.goldenhome.infrastructure.UserRepository
import kr.co.goldenhome.signup.dto.SignupRequest
import spock.lang.Specification

class SignupManagerSpec extends Specification {

    SignupManager signupManager
    def userRepository = Mock(UserRepository)
    def passwordProcessor = Mock(PasswordProcessor)

    def setup() {
        signupManager = new SignupManager(userRepository, passwordProcessor)
    }

    def "isLoginIdDuplicated - 로그인 ID가 이미 존재하면 CustomException 을 던져야 한다"() {
        given:
        def existingLoginId = "gucoding1234"

        when:
        signupManager.isLoginIdDuplicated(existingLoginId)

        then:
        CustomException e = thrown()
        e.getErrorCode() == ErrorCode.DUPLICATED_LOGIN_ID
        e.getOrigin().contains("SignupManager.isLoginIdDuplicated")

        1 * userRepository.existsByLoginId(existingLoginId) >> true
    }

    def "isLoginIdDuplicated - 로그인 ID가 존재하지 않으면 예외를 던지지 않아야 한다"() {
        given:
        def newLoginId = "newuser123"

        when: "테스트 대상 메서드를 호출할 때"
        signupManager.isLoginIdDuplicated(newLoginId)

        then:
        noExceptionThrown()

        1 * userRepository.existsByLoginId(newLoginId) >> false
    }

    def "createUser - 비밀번호를 인코딩하고 UserRepository 를 호출한다"() {
        given:
        def givenSignup = new SignupRequest("gucoding1234", "gucoding@1234", "1234", "01012345555")

        when:
        signupManager.createUser(givenSignup)

        then:
        1 * passwordProcessor.encode(givenSignup.password()) >> "encodedPassword"

        and:
        1 * userRepository.save(*_) >> {
            User user ->
                assert user.loginId == givenSignup.loginId()
                assert user.email == givenSignup.email()
                assert user.password == "encodedPassword"
                assert user.userRole == UserRole.USER
                assert user.userStatus == UserStatus.ACTIVE
        }
    }

}
