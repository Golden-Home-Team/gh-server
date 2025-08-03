package kr.co.goldenhome.authentication.docs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.authentication.dto.ResetPasswordRequest
import kr.co.goldenhome.authentication.dto.VerificationConfirmRequest
import kr.co.goldenhome.authentication.dto.VerificationConfirmResponse
import kr.co.goldenhome.authentication.dto.VerificationConfirmServiceResponse
import kr.co.goldenhome.authentication.dto.VerificationRequest
import kr.co.goldenhome.authentication.dto.VerificationResponse
import kr.co.goldenhome.authentication.service.AuthRecoveryService
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
class AuthRecoveryControllerDocsSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    AuthRecoveryService authRecoveryService = Mock()

    def "계정 찾기 - 인증요청"() {
        given:
        def request = new VerificationRequest("EMAIL", "gucoding1234@google.com")
        def expectedResponse = new VerificationResponse("12345678")
        authRecoveryService.requestVerification(*_) >> expectedResponse
        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/recover/verification-request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(document("recovery-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("type").type(JsonFieldType.STRING)
                                        .description("로그인 아이디 찾는 방법 e.g. EMAIL, PHONE"),
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

    def "계정 찾기 - 인증확인"() {
        given:
        def request = new VerificationConfirmRequest("EMAIL", "gucoding1234@google.com", "12345678", "testLoginId")
        def expectedResponse = new VerificationConfirmResponse(LocalDateTime.of(2000, 1, 1, 10, 10), "gucoding1234", "abc-123")
        authRecoveryService.confirm(*_) >> expectedResponse
        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/recover/verification-confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(document("recovery-confirm",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("type").type(JsonFieldType.STRING)
                                        .description("로그인 아이디 찾는 방법 e.g. EMAIL, PHONE"),
                                fieldWithPath("contact").type(JsonFieldType.STRING)
                                        .description("연락처 이메일주소 or 전화번호"),
                                fieldWithPath("verificationCode").type(JsonFieldType.STRING)
                                        .description("인증코드"),
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                        .description("로그인 아이디(비밀번호 찾기 요청일 경우 필요합니다.) [Optional]").optional()
                        ),
                        responseFields(
                                fieldWithPath("createdAt").type(JsonFieldType.STRING)
                                        .description("가입일"),
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                        .description("로그인 아이디"),
                                fieldWithPath("resetPasswordToken").type(JsonFieldType.STRING)
                                        .description("비밀번호 재설정 시 필요한 토큰입니다.")
                        )
                )
                )

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.createdAt').value(LocalDateTime.of(2000, 1, 1, 10, 10))
            MockMvcResultMatchers.jsonPath('$.loginId').value("gucoding1234")
            MockMvcResultMatchers.jsonPath('$.resetPasswordToken').value("abc-123")
        }

    }

    def "비밀번호 재설정"() {
        given:
        def request = new ResetPasswordRequest("abcd-1234","test1234", "1234", "1234")

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/recover/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(document("password-change",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("resetPasswordToken").type(JsonFieldType.STRING)
                                        .description("비밀번호 재설정 검증 토큰"),
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                        .description("로그인 아이디"),
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
}
