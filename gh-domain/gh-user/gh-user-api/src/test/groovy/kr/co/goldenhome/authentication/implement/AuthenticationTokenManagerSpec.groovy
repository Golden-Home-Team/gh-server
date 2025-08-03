package kr.co.goldenhome.authentication.implement

import kr.co.goldenhome.infrastructure.TokenRepository
import spock.lang.Specification

import java.time.Duration

class AuthenticationTokenManagerSpec extends Specification {

    AuthenticationTokenManager authenticationTokenManager
    TokenRepository refreshTokenRepository = Mock()

    def setup() {
        authenticationTokenManager = new AuthenticationTokenManager("eL3=kPzQxRQsT0uV2wX1yZ3aB4cD5eF6gH7if8kL9ma0oP1qR0sT3uV4wX5yZKaB7c", refreshTokenRepository)
    }

    def "create - RefreshTokenRepository 를 호출한다"() {
        given:
        def givenUserId = 1L

        when:
        authenticationTokenManager.create(givenUserId)

        then:
        1 * refreshTokenRepository.save(*_) >> {
            String userId, String refreshToken, Duration duration ->
                userId == String.valueOf(givenUserId)
        }
    }
}
