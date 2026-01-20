package kr.co.goldenhome.authentication.docs

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.messaging.FirebaseMessaging
import kr.co.goldenhome.FcmConfig
import kr.co.goldenhome.auth.UserPrincipal
import kr.co.goldenhome.authentication.dto.FindLoginIdRequest
import kr.co.goldenhome.authentication.dto.FindLoginIdResponse
import kr.co.goldenhome.authentication.dto.ResetEmailRequest
import kr.co.goldenhome.authentication.dto.ResetPasswordRequest
import kr.co.goldenhome.authentication.dto.ResetPhoneNumberRequest
import kr.co.goldenhome.authentication.dto.VerificationRequest
import kr.co.goldenhome.authentication.dto.VerificationResponse
import kr.co.goldenhome.authentication.service.AuthService
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

import java.time.LocalDate
import java.time.LocalDateTime

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class AuthControllerDocsSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    AuthService authService = Mock()

    @SpringBean
    FcmConfig fcmConfig = Mock()

    @SpringBean
    FirebaseMessaging firebaseMessaging = Mock()

    def "인증요청"() {
        given:
        def request = new VerificationRequest("EMAIL", "gucoding1234@google.com")
        def expectedResponse = new VerificationResponse("12345678")
        authService.requestVerification(*_) >> expectedResponse
        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/verification-request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(document("verification-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("type").type(JsonFieldType.STRING)
                                        .description("인증타입(EMAIL, PHONE)"),
                                fieldWithPath("contact").type(JsonFieldType.STRING)
                                        .description("연락처 이메일주소 or 전화번호")
                        ),
                        responseFields(
                                fieldWithPath("verificationCode").type(JsonFieldType.STRING)
                                        .description("인증코드")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.verificationCode').value("12345678")
        }

    }


    def "비밀번호 재설정"() {
        given:
        def request = new ResetPasswordRequest("EMAIL","abcd-1234","test1234", "1234", "1234")

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/reset-password")
                .principal(new UserPrincipal(1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(document("reset-password",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("type").type(JsonFieldType.STRING)
                                        .description("인증타입(EMAIL, PHONE)"),
                                fieldWithPath("contact").type(JsonFieldType.STRING)
                                        .description("연락처 이메일주소 or 전화번호"),
                                fieldWithPath("verificationCode").type(JsonFieldType.STRING)
                                        .description("인증코드"),
                                fieldWithPath("newPassword").type(JsonFieldType.STRING)
                                        .description("새 비밀번호"),
                                fieldWithPath("confirmPassword").type(JsonFieldType.STRING)
                                        .description("비밀번호 확인")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )
                ))


        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value(true)
        }
    }

    def "이메일 재설정"() {
        given:
        def request = new ResetEmailRequest("EMAIL","abc@naver.com", "1234", "rkdie23@naver.com")

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/reset-email")
                .principal(new UserPrincipal(1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(document("reset-email",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("type").type(JsonFieldType.STRING)
                                        .description("인증타입(EMAIL, PHONE)"),
                                fieldWithPath("contact").type(JsonFieldType.STRING)
                                        .description("연락처 이메일주소 or 전화번호"),
                                fieldWithPath("verificationCode").type(JsonFieldType.STRING)
                                        .description("인증코드"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("새 이메일")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )
                ))


        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value(true)
        }
    }

    def "전화번호 재설정"() {
        given:
        def request = new ResetPhoneNumberRequest("EMAIL","abc@naver.com", "1234", "01030245532")

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/reset-phone")
                .principal(new UserPrincipal(1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(document("reset-phone",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("type").type(JsonFieldType.STRING)
                                        .description("인증타입(EMAIL, PHONE)"),
                                fieldWithPath("contact").type(JsonFieldType.STRING)
                                        .description("연락처 이메일주소 or 전화번호"),
                                fieldWithPath("verificationCode").type(JsonFieldType.STRING)
                                        .description("인증코드"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                                        .description("새 전화번호")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )
                ))


        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value(true)
        }
    }

    def "아이디찾기"() {
        given:
        def request = new FindLoginIdRequest("EMAIL","abc@naver.com", "1234")
        authService.findLoginId(*_) >> new FindLoginIdResponse(LocalDateTime.now(), "id123")

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/find-login-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(document("find-login-id",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("type").type(JsonFieldType.STRING)
                                        .description("인증타입(EMAIL, PHONE)"),
                                fieldWithPath("contact").type(JsonFieldType.STRING)
                                        .description("연락처 이메일주소 or 전화번호"),
                                fieldWithPath("verificationCode").type(JsonFieldType.STRING)
                                        .description("인증코드")
                        ),
                        responseFields(
                                fieldWithPath("createdAt").type(JsonFieldType.STRING)
                                        .description("가입일"),
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                        .description("아이디"),
                        )
                ))


        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }
}
