package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.DailyExerciseRequest
import kr.co.goldenhome.dto.DailyExerciseResponse
import kr.co.goldenhome.dto.DailyExerciseUpdateRequest
import kr.co.goldenhome.dto.DailyRehabilitationRequest
import kr.co.goldenhome.dto.DailyRehabilitationResponse
import kr.co.goldenhome.dto.DailyRehabilitationUpdateRequest
import kr.co.goldenhome.service.DailyRehabilitationService
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
import java.time.LocalTime

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
class DailyRehabilitationControllerSpecDocs extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    DailyRehabilitationService dailyRehabilitationService = Mock()

    def "오늘의 재활 작성"() {

        given:
        def givenFacilityId = 1L
        def givenRequest = new DailyRehabilitationRequest(LocalDate.now(), "", List.of(new DailyExerciseRequest("", LocalTime.now(), LocalTime.now())))

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/communities/{facilityId}/daily-rehab", givenFacilityId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest))
        )
                .andDo(document("daily-rehab-write",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        requestFields(
                                fieldWithPath("recordDate").description("기록일 e.g. 2025-08-19").type(JsonFieldType.STRING),
                                fieldWithPath("treatment").description("치료 내용").type(JsonFieldType.STRING),
                                fieldWithPath("dailyExerciseRequests").description("운동 정보").type(JsonFieldType.ARRAY),
                                fieldWithPath("dailyExerciseRequests[].content").description("운동 내용").type(JsonFieldType.STRING),
                                fieldWithPath("dailyExerciseRequests[].startTime").description("시작시간").type(JsonFieldType.STRING),
                                fieldWithPath("dailyExerciseRequests[].endTime").description("종료시간").type(JsonFieldType.STRING)
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

    def "오늘의 재활 수정"() {

        given:
        def givenFacilityId = 1L
        def givenDailyRehabId = 1L
        def givenRequest = new DailyRehabilitationUpdateRequest("", List.of(new DailyExerciseUpdateRequest(1L, "", LocalTime.now(), LocalTime.now())))

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.put("/api/communities/{facilityId}/daily-rehab/{dailyRehabId}", givenFacilityId, givenDailyRehabId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest))
        )
                .andDo(document("daily-rehab-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디"),
                                parameterWithName("dailyRehabId").description("오늘의 재활 아이디")
                        ),
                        requestFields(
                                fieldWithPath("treatment").description("치료 내용").type(JsonFieldType.STRING),
                                fieldWithPath("dailyExerciseUpdateRequests").description("운동 정보").type(JsonFieldType.ARRAY),
                                fieldWithPath("dailyExerciseUpdateRequests[].dailyExerciseId").description("오늘의 재활 (운동) 아이디").type(JsonFieldType.NUMBER),
                                fieldWithPath("dailyExerciseUpdateRequests[].content").description("운동 내용").type(JsonFieldType.STRING),
                                fieldWithPath("dailyExerciseUpdateRequests[].startTime").description("시작시간").type(JsonFieldType.STRING),
                                fieldWithPath("dailyExerciseUpdateRequests[].endTime").description("종료시간").type(JsonFieldType.STRING)
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

    def "오늘의 재활 - 요일별로 읽기"() {

        given:
        def givenFacilityId = 1L
        def givenResponse = new DailyRehabilitationResponse(
                1L,
                LocalDate.of(2025, 10, 10),
                "특이사항 없음",
                List.of(new DailyExerciseResponse(1L, "운동 1회", LocalTime.of(10,10), LocalTime.of(11, 30)))
        )
        dailyRehabilitationService.readByDayOfWeek(givenFacilityId, DayOfWeek.MONDAY) >> givenResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/communities/{facilityId}/daily-rehab", givenFacilityId)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("dayOfWeek", "MONDAY")
        )
                .andDo(document("daily-rehab-readByDayOfWeek",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("dayOfWeek").description("MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY")
                        ),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("오늘의 재활 아이디"),
                                fieldWithPath("recordDate").type(JsonFieldType.STRING)
                                        .description("기록일"),
                                fieldWithPath("treatment").type(JsonFieldType.STRING)
                                        .description("치료내용"),
                                fieldWithPath("dailyExerciseResponses").type(JsonFieldType.ARRAY)
                                        .description("오늘의 운동 정보들"),
                                fieldWithPath("dailyExerciseResponses[].id").type(JsonFieldType.NUMBER)
                                        .description("오늘의 운동 아이디"),
                                fieldWithPath("dailyExerciseResponses[].id").type(JsonFieldType.NUMBER)
                                        .description("오늘의 운동 아이디"),
                                fieldWithPath("dailyExerciseResponses[].content").type(JsonFieldType.STRING)
                                        .description("오늘의 운동 내용"),
                                fieldWithPath("dailyExerciseResponses[].startTime").type(JsonFieldType.STRING)
                                        .description("오늘의 운동 시작시간"),
                                fieldWithPath("dailyExerciseResponses[].endTime").type(JsonFieldType.STRING)
                                        .description("오늘의 운동 종료시간"),
                        )
                ))
        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }
}
