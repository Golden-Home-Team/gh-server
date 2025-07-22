package kr.co.goldenhome.docs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.ReviewImageApiResponse
import kr.co.goldenhome.dto.ReviewRequest
import kr.co.goldenhome.dto.ReviewResponse
import kr.co.goldenhome.service.ReviewService
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ReviewControllerDocsSpec extends Specification {

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
                .andDo(document("review-write",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("리뷰 댓글"),
                                fieldWithPath("score").type(JsonFieldType.NUMBER)
                                        .description("리뷰 점수 : 최소1 ~ 최대5"),
                                fieldWithPath("formattedImageNames").type(JsonFieldType.ARRAY)
                                        .description("포맷된 이미지 이름 리스트"),
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
        ).andDo(document("review-readAll",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("facilityId").description("시설 아이디"),
                ),
                queryParameters(
                        parameterWithName("sort").description("정렬조건 기본값은 score(추천순)이고 이외의 값을 보내주시면 최신순입니다."),
                        parameterWithName("lastId").description("마지막으로 조회한 리뷰 아이디 (lastScore 와 동반)"),
                        parameterWithName("lastScore").description("마지막으로 조회한 리뷰 별점 (lastId 와 동반)"),
                        parameterWithName("pageSize").description("페이지 사이즈 default 20"),
                ),
                responseFields(
                        fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                .description("리뷰목록"),
                        fieldWithPath("[].writerId").type(JsonFieldType.NUMBER)
                                .description("작성자 아이디"),
                        fieldWithPath("[].writerName").type(JsonFieldType.STRING)
                                .description("작성자 로그인 아이디"),
                        fieldWithPath("[].content").type(JsonFieldType.STRING)
                                .description("댓글"),
                        fieldWithPath("[].score").type(JsonFieldType.NUMBER)
                                .description("별점 1 ~ 5"),
                        fieldWithPath("[].reviewImageApiResponses[]").type(JsonFieldType.ARRAY)
                                .description("리뷰이미지 정보 목록"),
                        fieldWithPath("[].reviewImageApiResponses[].id").type(JsonFieldType.NUMBER)
                                .description("리뷰이미지 아이디"),
                        fieldWithPath("[].reviewImageApiResponses[].formattedName").type(JsonFieldType.STRING)
                                .description("리뷰이미지이름 (포맷 및 인코딩 됨)"),
                        fieldWithPath("[].reviewImageApiResponses[].imageUrl").type(JsonFieldType.STRING)
                                .description("리뷰이미지 주소"),
                        fieldWithPath("[].monthsAgo").type(JsonFieldType.NUMBER)
                                .description("리뷰 작성 후 기간 (N개월 후)")


                )
        ))

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
