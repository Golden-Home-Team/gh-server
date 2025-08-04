package kr.co.goldenhome.account.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.account.service.AccountService
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
class AccountControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    AccountService accountService = Mock()


    def "회원탈퇴"() {

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/account/withdraw")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }

    def "로그아웃"() {

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/account/logout")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }

    }
}
