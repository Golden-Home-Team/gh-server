package kr.co.goldenhome.authentication.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.authentication.dto.LoginRequest
import kr.co.goldenhome.authentication.dto.LoginResponse
import kr.co.goldenhome.authentication.service.AuthenticationService
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
class AuthenticationControllerSpec extends Specification {

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

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.accessToken').value(expectedLoginResponse.accessToken())
            MockMvcResultMatchers.jsonPath('$.refreshToken').value(expectedLoginResponse.refreshToken())
        }
    }
}
