package kr.co.goldenhome.signup.docs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.Signup
import kr.co.goldenhome.entity.EmailVerification
import kr.co.goldenhome.entity.User
import kr.co.goldenhome.enums.EmailVerificationType
import kr.co.goldenhome.enums.UserRole
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

    def "회원가입 성공"() {
        given:
        def request = new SignupRequest("구코딩", "gucoding@naver.com", "a12345678", "a12345678")
        def expectedUser = User.create("구코딩", "gucoding@naver.com", "a12345678", UserRole.USER)
        def expectedVerificationCode = "TEST_VERIFICATION_CODE_123"
        def expectedEmailVerification = EmailVerification.create(1L, "gucoding@naver.com", EmailVerificationType.SIGN_UP)

        1 * signupService.signup(_ as Signup) >> expectedUser
        1 * signupService.sendEmailForVerification(_ as User) >> expectedEmailVerification

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

                .andDo(document("user-signup",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                .description("이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호 (영문 + 숫자 8자 이상)"),
                                fieldWithPath("confirmPassword").type(JsonFieldType.STRING)
                                        .description("비밀번호확인")
                        ),
                        responseFields(fieldWithPath("verificationCode").type(JsonFieldType.STRING)
                                .description("인증코드"))))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.verificationCode').value(expectedVerificationCode)
        }
    }

    def "회원가입 인증 성공"() {
        given:
        def givenCode = "givenCode"

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/signup/verify")
                .queryParam("code", givenCode))
                .andDo(document("user-verify",
                        preprocessResponse(prettyPrint()),
                        queryParameters(parameterWithName("code").description("인증코드")),
                        responseFields(fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("성공여부"))))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value(true)
        }
    }


}
