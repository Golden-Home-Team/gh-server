package kr.co.goldenhome.authentication.impl

import exception.CustomException
import exception.ErrorCode
import kr.co.goldenhome.entity.User
import kr.co.goldenhome.infrastructure.PasswordProcessor
import kr.co.goldenhome.infrastructure.UserRepository
import spock.lang.Specification
import spock.lang.Unroll

class UserAuthenticationManagerSpec extends Specification {

    UserAuthenticationManager userAuthenticationManager
    UserRepository userRepository = Mock()
    PasswordProcessor passwordProcessor = Mock()

    def setup() {
        userAuthenticationManager = new UserAuthenticationManager(userRepository, passwordProcessor)
    }

    def "authenticate - UserRepository.findByEmail, PasswordProcessor.matches 를 호출한다" () {
        given:
        def givenEmail = "gucoding@naver.com"
        def givenPassword = "1234"
        def expectedUser = User.builder().password("1234").build()

        when:
        userAuthenticationManager.authenticate(givenEmail, givenPassword)

        then:
        1 * userRepository.findByEmail(givenEmail) >> {
            String email ->
                email == givenEmail
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
        1 * userRepository.findByEmail(*_) >> findByEmailResult
        (findByEmailResult.isPresent() ? 1 : 0) * passwordProcessor.matches(*_) >> matchesResult

        when:
        userAuthenticationManager.authenticate("gucoding@navercom", "1233")

        then:
        def exception = thrown(CustomException)
        exception.getErrorCode() == expectedErrorCode

        where:
        description       | findByEmailResult            | matchesResult | expectedErrorCode
        "존재하지 않는 사용자" | Optional.empty()             | false         | ErrorCode.AUTHENTICATION_FAILED
        "비밀번호 불일치"     |  Optional.of(User.builder().build()) | false         | ErrorCode.AUTHENTICATION_FAILED
    }


}
