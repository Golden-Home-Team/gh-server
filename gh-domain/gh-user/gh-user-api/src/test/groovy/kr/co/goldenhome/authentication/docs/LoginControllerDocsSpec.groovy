package kr.co.goldenhome.authentication.docs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.SocialPlatform
import kr.co.goldenhome.authentication.dto.LoginRequest
import kr.co.goldenhome.authentication.dto.LoginResponse
import kr.co.goldenhome.authentication.dto.RefreshRequest
import kr.co.goldenhome.authentication.service.LoginService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class LoginControllerDocsSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    LoginService authenticationService = Mock()

    def "로그인 성공"() {

        given:
        def request = new LoginRequest("gucoding", "1234")
        def expectedLoginResponse = new LoginResponse("accessToken", "refreshToken")

        1 * authenticationService.login(_ as LoginRequest) >> expectedLoginResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(document("user-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                        .description("아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                        .description("엑세스 토큰"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                        .description("리프레시 토큰")
                        )
                )
                )

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.accessToken').value(expectedLoginResponse.accessToken())
            MockMvcResultMatchers.jsonPath('$.refreshToken').value(expectedLoginResponse.refreshToken())
        }
    }

    def "소셜 로그인 인가코드 요청 성공 - 지정된 provider_type 으로 리다이렉트"() {
        given:
        def providerType = "KAKAO"
        def redirectUri = "https://kauth.kakao.com/oauth/authorize?client_id=your_client_id&redirect_uri=your_redirect_uri&response_type=code"

        1 * authenticationService.getAuthorizationCode(SocialPlatform.KAKAO) >> ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirectUri)
                .build()

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/social/login/initiate")
                .param("provider_type", providerType))
                .andDo(document("social-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("provider_type").description("소셜 로그인 종류 e.g. KAKAO, NAVER"),
                        )))

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
                .andDo(document("social-login-callback",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("provider_type").description("소셜 로그인 종류 e.g. KAKAO, NAVER"),
                                parameterWithName("code").description("인가코드"),
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                        .description("엑세스 토큰"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                        .description("리프레시 토큰")
                        )))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.accessToken').value(expectedLoginResponse.accessToken())
            MockMvcResultMatchers.jsonPath('$.refreshToken').value(expectedLoginResponse.refreshToken())
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
                .andDo(document("token-refresh",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                        .description("리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                        .description("엑세스 토큰"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                        .description("리프레시 토큰 : 유효기간이 1주일 이하면 새로운 토큰이 전달됩니다.")
                        )
                )
                )

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.accessToken').value(expectedLoginResponse.accessToken())
            MockMvcResultMatchers.jsonPath('$.refreshToken').value(expectedLoginResponse.refreshToken())
        }
    }
}
