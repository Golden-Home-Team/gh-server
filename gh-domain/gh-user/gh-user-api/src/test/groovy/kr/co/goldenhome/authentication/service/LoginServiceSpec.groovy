package kr.co.goldenhome.authentication.service

import kr.co.goldenhome.SocialPlatform
import kr.co.goldenhome.authentication.dto.LoginRequest
import kr.co.goldenhome.authentication.dto.LoginResponse
import kr.co.goldenhome.authentication.implement.AuthenticationTokenManager
import kr.co.goldenhome.authentication.implement.UserAuthenticationManager
import kr.co.goldenhome.entity.User
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class LoginServiceSpec extends Specification {

    LoginService authenticationService
    UserAuthenticationManager userAuthenticationManager = Mock()
    AuthenticationTokenManager authenticationTokenManager = Mock()

    def setup() {
        authenticationService = new LoginService(userAuthenticationManager, authenticationTokenManager)
    }

    def "login - UserAuthenticationManager, AuthenticationTokenManager 를 호출한다"() {

        given:
        def givenEmail = "gucoding@naver.com"
        def givenPassword = "1234"
        def givenLoginRequest = new LoginRequest(givenEmail, givenPassword)
        def expectedUser = User.builder().id(1L).build()
        def expectedLoginResponse = new LoginResponse("accessToken", "refreshToken")

        when:
        authenticationService.login(givenLoginRequest)

        then:
        1 * userAuthenticationManager.authenticate(*_) >> {
            String email, String password ->
                email == givenEmail
                password == givenPassword
                expectedUser
        }

        and:
        1 * authenticationTokenManager.create(*_) >> {
            Long userId ->
                userId == expectedUser.id
                expectedLoginResponse
        }
    }

    def "getAuthorizationCode - UserAuthenticationManager 를 호출한다"() {
        given:
        def givenSocialPlatform = SocialPlatform.KAKAO

        when:
        authenticationService.getAuthorizationCode(givenSocialPlatform)

        then:
        1 * userAuthenticationManager.getAuthorizationCode(*_) >> {
            SocialPlatform socialPlatform ->
                socialPlatform == givenSocialPlatform
                ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "url").build()
        }
    }

    def "getUserInfo - UserAuthenticationManager, AuthenticationTokenManager 를 호출한다"() {
        given:
        def givenSocialPlatform = SocialPlatform.KAKAO
        def givenAuthorizationCode = "12qw34"

        when:
        authenticationService.getUserInfo(givenSocialPlatform, givenAuthorizationCode)

        then:
        1 * userAuthenticationManager.getUserInfo(*_) >> {
            SocialPlatform socialPlatform, String authorizationCode ->
                socialPlatform == givenSocialPlatform
                authorizationCode == givenAuthorizationCode
                User.builder().build()
        }

        and:
        1 * authenticationTokenManager.create(*_)
    }

    def "refresh - AuthenticationTokenManager 를 호출한다"() {
        given:
        def givenRefreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWZyZXNoVG9rZW4iLCJ1c2VySWQiOjEsImlhdCI6MTc1MTQwMDIxNCwiZXhwIjoxNzUzOTkyMjE0fQ.Lwvnff-TCdCY4OsQYJwPmB2daADbvJu9uexQiojj0RE"
        def expectedResponse = new LoginResponse("accessToken", "refreshToken")

        when:
        authenticationService.refresh(givenRefreshToken)

        then:
        1 * authenticationTokenManager.refresh(*_) >> {
            String refreshToken ->
                refreshToken == givenRefreshToken
                expectedResponse
        }
    }
}
