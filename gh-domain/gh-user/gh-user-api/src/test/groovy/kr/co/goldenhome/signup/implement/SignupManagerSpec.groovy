package kr.co.goldenhome.signup.implement

import kr.co.goldenhome.dto.Signup
import kr.co.goldenhome.entity.User
import kr.co.goldenhome.enums.UserRole
import kr.co.goldenhome.enums.UserStatus
import kr.co.goldenhome.infrastructure.PasswordProcessor
import kr.co.goldenhome.infrastructure.UserRepository
import spock.lang.Specification

class SignupManagerSpec extends Specification {

    SignupManager signupManager
    def userRepository = Mock(UserRepository)
    def passwordProcessor = Mock(PasswordProcessor)

    def setup() {
        signupManager = new SignupManager(userRepository, passwordProcessor)
    }

    def "createUser - 비밀번호를 인코딩하고 UserRepository 를 호출한다"() {
        given:
        def givenSignup = new Signup("구코딩", "gucoding@1234", "1234")

        when:
        signupManager.createUser(givenSignup)

        then:
        1 * passwordProcessor.encode(givenSignup.password()) >> "encodedPassword"

        and:
        1 * userRepository.save(*_) >> {
            User user ->
                assert user.username == givenSignup.username()
                assert user.email == givenSignup.email()
                assert user.password == "encodedPassword"
                assert user.userRole == UserRole.USER
                assert user.userStatus == UserStatus.PENDING
        }
    }

    def "active - UserRepository 를 호출한다"() {
        given:
        def givenUserId = 1L

        when:
        signupManager.active(givenUserId)

        then:
        1 * userRepository.findById(*_) >> {
            Long userId ->
                userId == givenUserId
                Optional.of(User.builder().id(givenUserId).userStatus(UserStatus.PENDING).build())
        }

    }
}
