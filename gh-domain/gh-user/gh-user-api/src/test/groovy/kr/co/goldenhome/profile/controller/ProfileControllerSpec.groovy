package kr.co.goldenhome.profile.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.ProfileImageApiResponse
import kr.co.goldenhome.profile.dto.ProfileImageRequest
import kr.co.goldenhome.profile.dto.ProfileResponse
import kr.co.goldenhome.profile.service.ProfileService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    ProfileService profileService = Mock()

    def "프로필 조회"() {

        given:
        def expectedProfileImageApiResponse = new ProfileImageApiResponse(1L, "123-abc-image1.jpg", "https://")
        def expectedResponse = new ProfileResponse(expectedProfileImageApiResponse, "구준형", "gucoding1234", "01012345678", "gucoding1234@naver.com")
        profileService.get(1L) >> expectedResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/profiles"))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$[0].profileImageApiResponse[0].id').value(1L)
            MockMvcResultMatchers.jsonPath('$[0].profileImageApiResponse[0].formattedName').value("123-abc-image1.jpg")
            MockMvcResultMatchers.jsonPath('$[0].profileImageApiResponse[0].imageUrl').value("https://")
            MockMvcResultMatchers.jsonPath('$.name').value("구준형")
            MockMvcResultMatchers.jsonPath('$.loginId').value("gucoding1234")
            MockMvcResultMatchers.jsonPath('$.phoneNumber').value("01012345678")
            MockMvcResultMatchers.jsonPath('$.email').value("gucoding1234@naver.com")
        }
    }

    def "프로필 이미지 생성"() {
        given:
        def givenRequest = new ProfileImageRequest("123-abc-image1.jpg")

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest))
        )

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value(true)
        }
    }
}
