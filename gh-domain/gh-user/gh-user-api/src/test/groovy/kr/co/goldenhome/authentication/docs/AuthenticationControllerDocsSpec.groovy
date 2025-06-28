package kr.co.goldenhome.authentication.docs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.authentication.dto.LoginRequest
import kr.co.goldenhome.authentication.dto.LoginResponse
import kr.co.goldenhome.authentication.service.AuthenticationService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class AuthenticationControllerDocsSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    AuthenticationService authenticationService = Mock()

    def "로그인 성공"() {

        given:
        def request = new LoginRequest("gucoding@naver.com", "1234")
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
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
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
}
