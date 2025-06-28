package kr.co.goldenhome.signup.service

import kr.co.goldenhome.dto.Signup
import kr.co.goldenhome.entity.EmailVerification
import kr.co.goldenhome.entity.User
import kr.co.goldenhome.enums.EmailVerificationType
import kr.co.goldenhome.enums.UserRole
import kr.co.goldenhome.signup.implement.EmailVerificationManager
import kr.co.goldenhome.signup.implement.SignupManager
import spock.lang.Specification

class SignupServiceSpec extends Specification {

    SignupService signupService

    def signupManager = Mock(SignupManager)
    def emailVerificationManager = Mock(EmailVerificationManager)

    void setup() {
        signupService = new SignupService(signupManager, emailVerificationManager)
    }

    def "signup - SignupManager.createUser() 를 호출한다"() {
        given:
        def givenSignup = new Signup("구코딩", "gucoding@1234", "1234", "1234")
        def expectedUser = User.create("구코딩", "gucoding@1234", "????", UserRole.USER)

        when:
        signupService.signup(givenSignup)

        then:
        1 * signupManager.createUser(*_) >> {
            Signup signup ->
                assert signup.username() == givenSignup.username()
                assert signup.email() == givenSignup.email()
                assert signup.password() == givenSignup.password()
                assert signup.confirmPassword() == givenSignup.confirmPassword()
                expectedUser
        }

    }

    def "sendEmailForVerification - EmailVerificationManager.create(), EmailVerificationManager.sendEmail() 을 호출한다"() {
        given:
        def givenUser = User.builder().id(1L).email("gucoding@1234").build()
        def expectedEmailVerification = EmailVerification.create(1L, "gucoding@1234", EmailVerificationType.SIGN_UP)

        1 * emailVerificationManager.create(givenUser.id, givenUser.email, EmailVerificationType.SIGN_UP) >> expectedEmailVerification

        when:
        def result = signupService.sendEmailForVerification(givenUser)

        then:
        1 * emailVerificationManager.sendEmail(expectedEmailVerification)

        result == expectedEmailVerification

    }

    def "verify - EmailVerificationManager.verify(), SignupManager.active() 를 호출한다"() {
        given:
        def givenCode = "givenCode"
        def givenUserId = 1L

        when:
        signupService.verify(givenCode)

        then:
        1 * emailVerificationManager.verify(givenCode) >> givenUserId

        and:
        1 * signupManager.active(givenUserId)
    }
}
