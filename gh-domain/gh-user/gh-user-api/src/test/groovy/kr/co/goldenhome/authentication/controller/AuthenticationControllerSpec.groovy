package kr.co.goldenhome.authentication.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.SocialPlatform
import kr.co.goldenhome.authentication.dto.LoginRequest
import kr.co.goldenhome.authentication.dto.LoginResponse
import kr.co.goldenhome.authentication.dto.RefreshRequest
import kr.co.goldenhome.authentication.service.AuthenticationService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    AuthenticationService authenticationService = Mock()

    def "로그인 성공"() {
        given:
        def request = new LoginRequest("gucoding1234", "1234")
        def expectedLoginResponse = new LoginResponse("accessToken", "refreshToken")

        1 * authenticationService.login(_ as LoginRequest) >> expectedLoginResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.accessToken').value(expectedLoginResponse.accessToken())
            MockMvcResultMatchers.jsonPath('$.refreshToken').value(expectedLoginResponse.refreshToken())
        }
    }

    def "소셜 로그인 인가코드 요청 성공 - 지정된 provider_type으로 리다이렉트"() {
        given:
        def providerType = "KAKAO"
        def redirectUri = "https://kauth.kakao.com/oauth/authorize?client_id=your_client_id&redirect_uri=your_redirect_uri&response_type=code"

        1 * authenticationService.getAuthorizationCode(SocialPlatform.KAKAO) >> ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirectUri)
                .build()

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/social/login/initiate")
                .param("provider_type", providerType))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isFound()
            MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, redirectUri)
        }
    }

    def "소셜 로그인 콜백 성공 - 유저 정보 가져와 로그인 응답 반환"() {
        given:
        def providerType = "NAVER"
        def authorizationCode = "some_naver_auth_code"
        def expectedLoginResponse = new LoginResponse("naverAccessToken", "naverRefreshToken")

        1 * authenticationService.getUserInfo(SocialPlatform.NAVER, authorizationCode) >> expectedLoginResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/social/login/callback")
                .param("provider_type", providerType)
                .param("code", authorizationCode))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.accessToken').value(expectedLoginResponse.accessToken())
            MockMvcResultMatchers.jsonPath('$.refreshToken').value(expectedLoginResponse.refreshToken())
        }
    }

    def "소셜 로그인 인가코드 요청 실패 - 유효하지 않은 provider_type"() {
        given:
        def invalidProviderType = "INVALID_PROVIDER"

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/social/login/initiate")
                .param("provider_type", invalidProviderType))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isBadRequest()
        }
    }

    def "액세스 토큰 갱신 성공"() {
        given:
        def request = new RefreshRequest("refreshToken")
        def expectedLoginResponse = new LoginResponse("accessToken", "newRefreshToken")
        1 * authenticationService.refresh(*_) >> expectedLoginResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.accessToken').value(expectedLoginResponse.accessToken())
            MockMvcResultMatchers.jsonPath('$.refreshToken').value(expectedLoginResponse.refreshToken())
        }
    }
}
