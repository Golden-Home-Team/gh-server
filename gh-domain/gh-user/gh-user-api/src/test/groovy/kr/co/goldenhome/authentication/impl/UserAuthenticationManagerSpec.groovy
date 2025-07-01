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


}
