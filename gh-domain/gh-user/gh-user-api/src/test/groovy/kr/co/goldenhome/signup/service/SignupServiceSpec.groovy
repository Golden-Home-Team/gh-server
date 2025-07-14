package kr.co.goldenhome.signup.service

import kr.co.goldenhome.signup.dto.SignupRequest
import kr.co.goldenhome.signup.implement.SignupManager
import spock.lang.Specification

class SignupServiceSpec extends Specification {

    SignupService signupService

    def signupManager = Mock(SignupManager)

    def setup() {
        signupService = new SignupService(signupManager)
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

    def "signup - SignupManager 를 호출한다"() {
        given:
        def givenSignup = new SignupRequest("gucoding1234", "gucoding@1234", "1234", "01012345555")

        when:
        signupService.signup(givenSignup)

        then:
        1 * signupManager.createUser(*_) >> {
            SignupRequest request ->
                assert request.loginId() == givenSignup.loginId()
                assert request.email() == givenSignup.email()
                assert request.password() == givenSignup.password()
                assert request.phoneNumber() == givenSignup.phoneNumber()
        }

    }

}
