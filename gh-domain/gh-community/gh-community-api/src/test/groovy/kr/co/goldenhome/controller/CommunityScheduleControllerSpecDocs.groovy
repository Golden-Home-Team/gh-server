package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.CommunityScheduleRequest
import kr.co.goldenhome.dto.CommunityScheduleResponse
import kr.co.goldenhome.dto.CommunityScheduleUpdateRequest
import kr.co.goldenhome.service.CommunityScheduleService
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
import java.time.Month

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
class CommunityScheduleControllerSpecDocs extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    CommunityScheduleService communityScheduleService = Mock()

    def "일정 작성"() {
        given:
        def givenFacilityId = 1L
        def givenRequest = new CommunityScheduleRequest(LocalDate.now(), "일정입니다.")

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/communities/{facilityId}/schedule", givenFacilityId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest))
        )
                .andDo(document("community-schedule-write",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        requestFields(
                                fieldWithPath("recordDate").description("기록일 e.g. 2025-08-19").type(JsonFieldType.STRING),
                                fieldWithPath("content").description("내용").type(JsonFieldType.STRING),
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

    def "일정 수정"() {
        given:
        def givenFacilityId = 1L
        def givenScheduleId = 1L
        def givenRequest = new CommunityScheduleUpdateRequest("일정수정")

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.put("/api/communities/{facilityId}/schedule/{scheduleId}", givenFacilityId, givenScheduleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest))
        )
                .andDo(document("community-schedule-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디"),
                                parameterWithName("scheduleId").description("일정 아이디")
                        ),
                        requestFields(
                                fieldWithPath("content").description("내용").type(JsonFieldType.STRING),
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

    def "일정 - 월별로 읽기"() {
        given:
        def givenFacilityId = 1L
        def givenResponse = List.of(new CommunityScheduleResponse(1L, LocalDate.now(), ""))
        communityScheduleService.readByMonth(givenFacilityId, Month.JULY) >> givenResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/communities/{facilityId}/schedule", givenFacilityId)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("month", "JULY")
        )
                .andDo(document("community-schedule-readByMonth",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                        .description("일정 정보들"),
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                        .description("일정 아이디"),
                                fieldWithPath("[].recordDate").type(JsonFieldType.STRING)
                                        .description("일정 기록일"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING)
                                        .description("일정 내용")
                        )
                ))
        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }
}
