package kr.co.goldenhome.signup.controller

import com.fasterxml.jackson.databind.ObjectMapper

import kr.co.goldenhome.signup.dto.SignupRequest
import kr.co.goldenhome.signup.service.SignupService
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

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class SignupControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    SignupService signupService = Mock() // ??? 왜 MockitoBean은 안될까t

    def "기존 사용자 존재여부 확인"() {
        given:
        def request = "gucoding1234"

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/signup/loginId/duplicated")
                .queryParam("loginId", request)
        )

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value("true")
        }
    }

    def "회원가입 성공"() {
        given:
        def request = new SignupRequest("gucoding1234", "gucoding@naver.com", "a12345678", "01012345555")

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value("true")
        }
    }
//
//    @Unroll
//    def "회원가입 실패 - #description"() {
//        given:
//        def requestJson = objectMapper.writeValueAsString(request)
//
//        when:
//        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/signup")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestJson))
//
//        then:
//        response.andExpect {
//            MockMvcResultMatchers.status().isBadRequest()
//            MockMvcResultMatchers.jsonPath('$.message').value(expectedErrorCode.message)
//        }
//
//        where:
//        description          | request                                                                        | expectedErrorCode
//        "잘못된 이메일 형식"         | new SignupRequest("구코딩", "gucoding", "password123!", "password123!")           | ErrorCode.INVALID_EMAIL
//        "비밀번호 불일치"           | new SignupRequest("구코딩", "gucoding@naver.com", "password123", "password124")   | ErrorCode.PASSWORD_MISMATCH
//        "잘못된 비밀번호 형식 - 특수문자" | new SignupRequest("구코딩", "gucoding@naver.com", "password123!", "password123!") | ErrorCode.INVALID_PASSWORD
//        "잘못된 비밀번호 형식 - 8자미만" | new SignupRequest("구코딩", "gucoding@naver.com", "1234", "1234")                 | ErrorCode.INVALID_PASSWORD
//    }

}
