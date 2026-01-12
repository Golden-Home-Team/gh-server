package kr.co.goldenhome.signup.docs

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.messaging.FirebaseMessaging
import kr.co.goldenhome.FcmConfig
import kr.co.goldenhome.signup.dto.SignupRequest
import kr.co.goldenhome.signup.service.SignupService
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*
import static org.springframework.restdocs.payload.PayloadDocumentation.*
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class SignupControllerDocsSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    SignupService signupService = Mock()

    @SpringBean
    FcmConfig fcmConfig = Mock()

    @SpringBean
    FirebaseMessaging firebaseMessaging = Mock()

    def "기존 사용자 아이디 존재여부 확인"() {
        given:
        def request = "gucoding1234"

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/signup/loginId/duplicated")
                .queryParam("loginId", request))
                .andDo(document("user-check-duplicated",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("loginId").description("로그인 아이디"),
                        ),
                        responseFields(fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("성공여부"))))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value("true")
        }
    }

    def "회원가입 성공"() {
        given:
        def request = new SignupRequest("gucoding1234", "gucoding@naver.com", "a12345678", "01012345555", "EMAIL", "122333")

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

                .andDo(document("user-signup",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                    .description("아이디"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                                        .description("전화번호"),
                                fieldWithPath("type").type(JsonFieldType.STRING)
                                        .description("인증타입(EMAIL, PHONE)"),
                                fieldWithPath("verificationCode").type(JsonFieldType.STRING)
                                        .description("인증코드")
                        ),
                        responseFields(fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("성공여부"))))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }

    def "기존 사용자 이메일 존재여부 확인"() {
        given:
        def request = "gucoding1234@naver.com"

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/signup/email/duplicated")
                .queryParam("email", request))
                .andDo(document("user-email-duplicated",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("email").description("이메일"),
                        ),
                        responseFields(fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("성공여부"))))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value("true")
        }
    }


}
