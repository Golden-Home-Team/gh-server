package kr.co.goldenhome.signup.service

import kr.co.goldenhome.authentication.implement.VerificationManagerFactory
import kr.co.goldenhome.signup.dto.SignupRequest
import kr.co.goldenhome.signup.implement.SignupManager
import spock.lang.Specification

class SignupServiceSpec extends Specification {

    SignupService signupService

    def signupManager = Mock(SignupManager)
    VerificationManagerFactory factory = Mock()

    def setup() {
        signupService = new SignupService(signupManager, factory)
    }

    def "isLoginDuplicated - SignupManager 를 호출한다"() {
        given:
        def givenLoginId = "gocuding1234"

        when:
        signupService.isLoginIdDuplicated(givenLoginId)

        then:
        1 * signupManager.isLoginIdDuplicated(*_) >> {
            String loginId ->
                loginId == givenLoginId
        }

    }

    def "signup - factory, SignupManager 를 호출한다"() {
        given:
        def givenSignup = new SignupRequest("gucoding1234", "gucoding@1234", "1234", "01012345555", "EMAIL", "12333")

        when:
        signupService.signup(givenSignup)

        then:
        1 * factory.confirm(*_)
        1 * signupManager.createUser(*_)

    }

    def "isEmailDuplicated - SignupManager 를 호출한다"() {
        given:
        def givenEmail = "gocuding1234@naver.com"

        when:
        signupService.isEmailDuplicated(givenEmail)

        then:
        1 * signupManager.isEmailDuplicated(*_) >> {
            String loginId ->
                loginId == givenEmail
        }

    }

}
