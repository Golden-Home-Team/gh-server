package kr.co.goldenhome.authentication.implement

import kr.co.goldenhome.infrastructure.RefreshTokenRepository
import spock.lang.Specification

import java.time.Duration

class AuthenticationTokenManagerSpec extends Specification {

    AuthenticationTokenManager authenticationTokenManager
    RefreshTokenRepository refreshTokenRepository = Mock()

    def setup() {
        authenticationTokenManager = new AuthenticationTokenManager("eL2=kPzQxR9sT0uV1wX2yZ3aB4cD5eF6gH7iJ8kL9mN0oP1qR2sT3uV4wX5yZ6aB7c", refreshTokenRepository)
    }

    def "create - RefreshTokenRepository 를 호출한다"() {
        given:
        def givenUserId = 1L

        when:
        authenticationTokenManager.create(givenUserId)

        then:
        1 * refreshTokenRepository.save(*_) >> {
            Long userId, String refreshToken, Duration expirationDuration->
                userId == givenUserId
                refreshToken
        }
    }
}
