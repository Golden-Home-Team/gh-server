package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.DailyMedicationRequest
import kr.co.goldenhome.dto.DailyMedicationResponse
import kr.co.goldenhome.dto.DailyMedicationUpdateRequest
import kr.co.goldenhome.service.DailyMedicationService
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
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
class DailyMedicationControllerSpecDocs extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    DailyMedicationService dailyMedicationService = Mock()

    def "오늘의 복약 작성"() {
        given:
        def givenFacilityId = 1L
        def givenRequest = new DailyMedicationRequest(LocalDate.now(), "아침 1회 복용", "특이사항 없음", "저녁 2회 복용")

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/communities/{facilityId}/daily-medication", givenFacilityId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest))
        )
                .andDo(document("daily-medication-write",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        requestFields(
                                fieldWithPath("recordDate").description("기록일 e.g. 2025-08-19").type(JsonFieldType.STRING),
                                fieldWithPath("morningContent").description("내용(아침)").type(JsonFieldType.STRING),
                                fieldWithPath("afternoonContent").description("내용(점심)").type(JsonFieldType.STRING),
                                fieldWithPath("nightContent").description("내용(저녁)").type(JsonFieldType.STRING)
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

    def "오늘의 복약 수정"() {
        given:
        def givenFacilityId = 1L
        def givenDailyMedicationId = 2L
        def givenRequest = new DailyMedicationUpdateRequest( "아침 1회 복용", "특이사항 없음", "저녁 2회 복용")

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.put("/api/communities/{facilityId}/daily-medication/{dailyMedicationId}", givenFacilityId, givenDailyMedicationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest))
        )
                .andDo(document("daily-medication-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디"),
                                parameterWithName("dailyMedicationId").description("오늘의 복약 아이디")
                        ),
                        requestFields(
                                fieldWithPath("morningContent").description("내용(아침)").type(JsonFieldType.STRING),
                                fieldWithPath("afternoonContent").description("내용(점심)").type(JsonFieldType.STRING),
                                fieldWithPath("nightContent").description("내용(저녁)").type(JsonFieldType.STRING)
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

    def "오늘의 복약 - 요일단위로 읽기"() {
        given:
        def givenFacilityId = 1L
        def givenResponse = new DailyMedicationResponse(
                1L,
                LocalDate.now(),
                "아침 1회 복용",
                "점심 2회 복용",
                "특이사항 없음",
                LocalDateTime.now()
        )
        dailyMedicationService.readByDayOfWeek(givenFacilityId, DayOfWeek.MONDAY) >> givenResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/communities/{facilityId}/daily-medication", givenFacilityId)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("dayOfWeek", "MONDAY")
        )
                .andDo(document("daily-medication-readByDayOfWeek",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디"),
                        ),
                        queryParameters(
                                parameterWithName("dayOfWeek").description("MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("오늘의 복약 아이디"),
                                fieldWithPath("recordDate").type(JsonFieldType.STRING)
                                        .description("기록일"),
                                fieldWithPath("morningContent").type(JsonFieldType.STRING)
                                        .description("오늘의 복약 아이디"),
                                fieldWithPath("afternoonContent").type(JsonFieldType.STRING)
                                        .description("오늘의 복약 아이디"),
                                fieldWithPath("nightContent").type(JsonFieldType.STRING)
                                        .description("오늘의 복약 아이디"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING)
                                        .description("생성일"),
                        )
                ))
        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value(true)
        }

    }
}
