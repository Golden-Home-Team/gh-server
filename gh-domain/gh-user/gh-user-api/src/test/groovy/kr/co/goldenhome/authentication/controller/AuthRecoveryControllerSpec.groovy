package kr.co.goldenhome.authentication.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.authentication.dto.VerificationConfirmRequest
import kr.co.goldenhome.authentication.dto.VerificationConfirmResponse
import kr.co.goldenhome.authentication.dto.VerificationRequest
import kr.co.goldenhome.authentication.dto.VerificationResponse
import kr.co.goldenhome.authentication.service.AuthRecoveryService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import java.time.LocalDateTime

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AuthRecoveryControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    AuthRecoveryService authRecoveryService = Mock()

    def "로그인 아이디 찾기 - 인증요청"() {
        given:
        def request = new VerificationRequest("EMAIL", "gucoding1234@google.com")
        def expectedResponse = new VerificationResponse("12345678")
        authRecoveryService.requestVerification(*_) >> expectedResponse
        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/recover/id/verification-request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.verificationCode').value("12345678")

        }

    }

    def "로그인 아이디 찾기 - 인증확인"() {
        given:
        def request = new VerificationConfirmRequest("EMAIL", "gucoding1234@google.com", "12345678")
        def expectedResponse = new VerificationConfirmResponse(LocalDateTime.of(2000, 1, 1, 10, 10), "gucoding1234")
        authRecoveryService.confirm(*_) >> expectedResponse
        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/recover/id/verification-confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.createdAt').value(LocalDateTime.of(2000, 1, 1, 10, 10))
            MockMvcResultMatchers.jsonPath('$.loginId').value("gucoding1234")

        }

    }
}
