package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.DailyShotImageInfoRequest
import kr.co.goldenhome.dto.DailyShotImageResponse
import kr.co.goldenhome.dto.DailyShotRequest
import kr.co.goldenhome.dto.DailyShotResponse
import kr.co.goldenhome.dto.DailyShotUpdateRequest
import kr.co.goldenhome.service.DailyShotService
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

import java.time.DayOfWeek
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
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class DailyShotControllerSpecDocs extends Specification{

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    DailyShotService dailyShotService = Mock()

    def "오늘의 한 컷 작성"() {
        given:
        def givenFacilityId = 1L
        def givenDailyShotImageInfoRequests = List.of(new DailyShotImageInfoRequest("INDIVIDUAL", "abc-123.jpg"))
        def givenRequest = new DailyShotRequest("건강상태양호", LocalDate.of(2025,10,10), givenDailyShotImageInfoRequests)

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/communities/{facilityId}/daily-shot", givenFacilityId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest))
        )
                .andDo(document("daily-shot-write",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("recordDate").type(JsonFieldType.STRING)
                                        .description("기록일 e.g.2025-08-19"),
                                fieldWithPath("dailyShotImageInfoRequests").type(JsonFieldType.ARRAY)
                                        .description("이미지 정보 리스트"),
                                fieldWithPath("dailyShotImageInfoRequests[].dailyShotType").type(JsonFieldType.STRING)
                                        .description("INDIVIDUAL, ORGANIZATION"),
                                fieldWithPath("dailyShotImageInfoRequests[].formattedImageName").type(JsonFieldType.STRING)
                                        .description("포맷팅된 이미지 이름")
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

    def "오늘의 한 컷 수정"() {
        given:
        def givenFacilityId = 1L
        def givenDailyShotId = 2L
        def givenDailyShotImageInfoRequests = List.of(new DailyShotImageInfoRequest("INDIVIDUAL", "abc-123.jpg"))
        def givenRequest = new DailyShotUpdateRequest("건강상태양호",  givenDailyShotImageInfoRequests)

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.put("/api/communities/{facilityId}/daily-shot/{dailyShotId}", givenFacilityId, givenDailyShotId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest))
        )
                .andDo(document("daily-shot-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디"),
                                parameterWithName("dailyShotId").description("오늘의 한 컷 아이디"),
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("dailyShotImageInfoRequests").type(JsonFieldType.ARRAY)
                                        .description("이미지 정보 리스트"),
                                fieldWithPath("dailyShotImageInfoRequests[].dailyShotType").type(JsonFieldType.STRING)
                                        .description("INDIVIDUAL, ORGANIZATION"),
                                fieldWithPath("dailyShotImageInfoRequests[].formattedImageName").type(JsonFieldType.STRING)
                                        .description("포맷팅된 이미지 이름")
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

    def "오늘의 한 컷 조회"() {
        given:
        def givenFacilityId = 1L
        def givenDailyShotImageResponse = new DailyShotImageResponse(1L,  "https://", LocalDateTime.of(2025,10,10,10,10))
        def givenResponse = new DailyShotResponse(1L, "양호한상태", LocalDate.of(2025,10,10), List.of(givenDailyShotImageResponse))
        dailyShotService.readByDayOfWeek(givenFacilityId, DayOfWeek.MONDAY) >> givenResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/communities/{facilityId}/daily-shot", givenFacilityId)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("dayOfWeek", "MONDAY")
        )
                .andDo(document("daily-shot-read",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        queryParameters(
                                parameterWithName("dayOfWeek").description("요일")
                        ),
                        responseFields(
                                fieldWithPath("dailyShotId").type(JsonFieldType.NUMBER)
                                        .description("오늘의 한 컷 아이디"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("성공여부"),
                                fieldWithPath("recordDate").type(JsonFieldType.STRING)
                                        .description("성공여부"),
                                fieldWithPath("dailyShotImageResponses").type(JsonFieldType.ARRAY)
                                        .description("이미지 정보 리스트"),
                                fieldWithPath("dailyShotImageResponses[].id").type(JsonFieldType.NUMBER)
                                        .description("이미지 아이디"),
                                fieldWithPath("dailyShotImageResponses[].imageUrl").type(JsonFieldType.STRING)
                                        .description("이미지 주소"),
                                fieldWithPath("dailyShotImageResponses[].createdAt").type(JsonFieldType.STRING)
                                        .description("생성일")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value(true)
        }

    }
}
