package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.CommunityNoticeRequest
import kr.co.goldenhome.dto.DailyDietImageInfoRequest
import kr.co.goldenhome.dto.DailyDietImageResponse
import kr.co.goldenhome.dto.DailyDietRequest
import kr.co.goldenhome.dto.DailyDietResponse
import kr.co.goldenhome.dto.DailyDietUpdateRequest
import kr.co.goldenhome.service.DailyDietService
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

import java.time.LocalDate
import java.time.LocalDateTime

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class DailyDietControllerSpecDocs extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    DailyDietService dailyDietService = Mock()

    def "오늘의 식단 작성"() {
        given:
        def givenFacilityId = 1L
        def givenDailyDietImageInfoRequests = List.of(new DailyDietImageInfoRequest("MORNING", "abc-123.jpg"))
        def givenRequest = new DailyDietRequest("영양가있는 식단이에요", LocalDate.of(2025, 10, 10), givenDailyDietImageInfoRequests)

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/communities/{facilityId}/daily-diet", givenFacilityId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest))
        )
                .andDo(document("daily-diet-write",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        requestFields(
                                fieldWithPath("content").description("제목").type(JsonFieldType.STRING),
                                fieldWithPath("recordDate").description("기록일 e.g. 2025-08-19").type(JsonFieldType.STRING),
                                fieldWithPath("dailyDietImageInfoRequests").description("이미지 정보 리스트").type(JsonFieldType.ARRAY),
                                fieldWithPath("dailyDietImageInfoRequests[].dailyDietType").description("MORNING, AFTERNOON, EVENING, MONTHLY").type(JsonFieldType.STRING),
                                fieldWithPath("dailyDietImageInfoRequests[].formattedImageName").description("포맷팅된 이미지 이름").type(JsonFieldType.STRING),
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

    def "오늘의 식단 수정"() {
        given:
        def givenFacilityId = 1L
        def givenDailyDietId = 2L
        def givenDailyDietImageInfoRequests = List.of(new DailyDietImageInfoRequest("MORNING", "abc-123.jpg"))
        def givenRequest = new DailyDietUpdateRequest("영양가있는 식단이에요", givenDailyDietImageInfoRequests)

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.put("/api/communities/{facilityId}/daily-diet/{dailyDietId}", givenFacilityId, givenDailyDietId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest))
        )
                .andDo(document("daily-diet-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디"),
                                parameterWithName("dailyDietId").description("오늘의 식단 아이디")
                        ),
                        requestFields(
                                fieldWithPath("content").description("제목").type(JsonFieldType.STRING),
                                fieldWithPath("dailyDietImageInfoRequests").description("이미지 정보 리스트").type(JsonFieldType.ARRAY),
                                fieldWithPath("dailyDietImageInfoRequests[].dailyDietType").description("MORNING, AFTERNOON, EVENING, MONTHLY(식단표)").type(JsonFieldType.STRING),
                                fieldWithPath("dailyDietImageInfoRequests[].formattedImageName").description("포맷팅된 이미지 이름").type(JsonFieldType.STRING),
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

    def "오늘의 식단 조회"() {
        given:
        def givenFacilityId = 1L
        def givenDailyDietId = 2L
        def givenImageInfoResponses = List.of(new DailyDietImageResponse(1L, "MORNING", "https://", LocalDateTime.of(2025, 10, 10, 10, 10)))
        def givenResponse = new DailyDietResponse(
                "영양가 있는 식단이에요",
                LocalDate.of(2025, 10, 10),
                LocalDateTime.of(2025, 10, 10, 10, 10),
                LocalDateTime.of(2025, 10, 10, 10, 10),
                givenImageInfoResponses
        )
        dailyDietService.read(givenDailyDietId) >> givenResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/communities/{facilityId}/daily-diet/{dailyDietId}", givenFacilityId, givenDailyDietId)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(document("daily-diet-read",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디"),
                                parameterWithName("dailyDietId").description("오늘의 식단 아이디")
                        ),
                        responseFields(
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("recordDate").type(JsonFieldType.STRING)
                                        .description("기록일"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING)
                                        .description("생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING)
                                        .description("수정일"),
                                fieldWithPath("dailyDietImageResponses").type(JsonFieldType.ARRAY)
                                        .description("이미지 정보 리스트"),
                                fieldWithPath("dailyDietImageResponses[].id").type(JsonFieldType.NUMBER)
                                        .description("이미지 아이디"),
                                fieldWithPath("dailyDietImageResponses[].dailyDietType").type(JsonFieldType.STRING)
                                        .description("MORNING, AFTERNOON, EVENING, MONTHLY(식단표)"),
                                fieldWithPath("dailyDietImageResponses[].imageUrl").type(JsonFieldType.STRING)
                                        .description("이미지 주소"),
                                fieldWithPath("dailyDietImageResponses[].createdAt").type(JsonFieldType.STRING)
                                        .description("이미지 생성일"),
                        )
                ))
        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.content').value(givenResponse.content())
            MockMvcResultMatchers.jsonPath('$.recordDate').value(givenResponse.recordDate())
            MockMvcResultMatchers.jsonPath('$.createdAt').value(givenResponse.createdAt())
            MockMvcResultMatchers.jsonPath('$.updatedAt').value(givenResponse.updatedAt())
            MockMvcResultMatchers.jsonPath('$.dailyDietImageResponses').value(givenResponse.dailyDietImageResponses())
            MockMvcResultMatchers.jsonPath('$.dailyDietImageResponses.id').value(givenResponse.dailyDietImageResponses().get(0).id())
            MockMvcResultMatchers.jsonPath('$.dailyDietImageResponses.dailyDietType').value(givenResponse.dailyDietImageResponses().get(0).dailyDietType())
            MockMvcResultMatchers.jsonPath('$.dailyDietImageResponses.imageUrl').value(givenResponse.dailyDietImageResponses().get(0).imageUrl())
            MockMvcResultMatchers.jsonPath('$.dailyDietImageResponses.createdAt').value(givenResponse.dailyDietImageResponses().get(0).createdAt())
        }


    }
}