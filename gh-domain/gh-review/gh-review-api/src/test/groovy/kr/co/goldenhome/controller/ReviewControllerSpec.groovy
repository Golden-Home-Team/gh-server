package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.ReviewImageApiResponse
import kr.co.goldenhome.dto.ReviewRequest
import kr.co.goldenhome.dto.ReviewResponse
import kr.co.goldenhome.service.ReviewService
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
class ReviewControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    ReviewService reviewService = Mock()

    def "리뷰작성"() {
        given:
        def request = new ReviewRequest("좋은 시설이야.", 5, List.of("image1"))

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/reviews/{facilityId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value(true)
        }

    }

    def "리뷰목록조회"() {
        given:
        def expectedResponse = List.of(new ReviewResponse(1L, "구준형", "좋은 시설이야", 5, List.of(new ReviewImageApiResponse(1L, "13e-je3-image1.jpg", "https://")), 1))
        reviewService.readAll(*_) >> expectedResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews/readAll/{facilityId}", 1L)
                .param("sort", "score")
                .param("lastId", "1")
                .param("lastScore", "1")
                .param("pageSize", "20")
        )

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$[0].writerId').value(expectedResponse.get(0).writerId())
            MockMvcResultMatchers.jsonPath('$[0].writerName').value(expectedResponse.get(0).writerName())
            MockMvcResultMatchers.jsonPath('$[0].content').value(expectedResponse.get(0).content())
            MockMvcResultMatchers.jsonPath('$[0].score').value(expectedResponse.get(0).score())
            MockMvcResultMatchers.jsonPath('$[0].reviewImageApiResponses[0].id').value(expectedResponse.get(0).reviewImageApiResponses().get(0).id())
            MockMvcResultMatchers.jsonPath('$[0].reviewImageApiResponses[0].formattedName').value(expectedResponse.get(0).reviewImageApiResponses().get(0).formattedName())
            MockMvcResultMatchers.jsonPath('$[0].reviewImageApiResponses[0].imageUrl').value(expectedResponse.get(0).reviewImageApiResponses().get(0).imageUrl())
            MockMvcResultMatchers.jsonPath('$[0].monthsAgo').value(expectedResponse.get(0).monthsAgo())

        }
    }
}