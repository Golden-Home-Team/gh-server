package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.auth.UserPrincipal
import kr.co.goldenhome.service.FacilityLikeService
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
class FacilityLikeControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    FacilityLikeService facilityLikeService = Mock()

    def "좋아요를 누른다"() {

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/likes/facility/{facilityId}", 1L)
                .principal(new UserPrincipal(1L))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value("true")
        }
    }

    def "좋아요를 취소한다"() {
        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/likes/facility/{facilityId}", 1L)
                .principal(new UserPrincipal(1L))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value("true")
        }
    }

}
