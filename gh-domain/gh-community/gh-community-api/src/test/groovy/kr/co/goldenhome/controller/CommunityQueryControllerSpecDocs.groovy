package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.CommunityCombinedResponse
import kr.co.goldenhome.dto.CommunityScheduleResponse
import kr.co.goldenhome.dto.DailyDietImageResponse
import kr.co.goldenhome.dto.DailyDietInfo
import kr.co.goldenhome.dto.DailyExerciseResponse
import kr.co.goldenhome.dto.DailyMedicationInfo
import kr.co.goldenhome.dto.DailyRehabilitationInfo
import kr.co.goldenhome.dto.DailyShotImageResponse
import kr.co.goldenhome.dto.DailyShotInfo
import kr.co.goldenhome.dto.MyCommunityResponse
import kr.co.goldenhome.dto.NoticeInfo
import kr.co.goldenhome.service.CommunityQueryService
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
import java.time.LocalTime

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class CommunityQueryControllerSpecDocs extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    CommunityQueryService communityQueryService = Mock()

    def "커뮤니티 유저인지 확인"() {
        given:
        def givenFacilityId = 1L
        communityQueryService.isCommunityUser(*_) >> true

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/communities/{facilityId}/check", givenFacilityId)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(document("community-user-check",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
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

    def "커뮤니티 메인화면 조회"() {
        given:
        def givenFacilityId = 1L
        def givenResponse = new CommunityCombinedResponse(
                new NoticeInfo(
                        1L,
                        "공지입니다.",
                        "공지내용입니다.",
                        LocalDateTime.now()
                ),
                new DailyDietInfo(
                        2L,
                        new DailyDietImageResponse(
                                3L,
                                "MORNING",
                                "https://",
                                LocalDateTime.now()
                        )
                ),
                new DailyShotInfo(
                        3L,
                        "잘나온 사진",
                        new DailyShotImageResponse(
                                1L,
                                "https://",
                                LocalDateTime.now()
                        )
                ),
                new DailyMedicationInfo(
                        1L,
                        "아침 2회",
                        "해당사항없음",
                        "특이사항없음"
                ),
                new DailyRehabilitationInfo(
                        1L,
                        "치료내용",
                        List.of(new DailyExerciseResponse(
                                1L,
                                "강운동 1회",
                                LocalTime.now(),
                                LocalTime.now()
                        ))
                ),
                List.of(new CommunityScheduleResponse(
                        1L,
                        LocalDate.now(),
                        ""
                )),
                "구매니저"
        )
        communityQueryService.read(givenFacilityId) >> givenResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/communities/{facilityId}", givenFacilityId)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(document("community-read",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        responseFields(
                                fieldWithPath("noticeInfo.noticeId").description("공지사항 아이디").optional(),
                                fieldWithPath("noticeInfo.noticeTitle").description("공지사항 제목").optional(),
                                fieldWithPath("noticeInfo.noticeContent").description("공지사항 내용").optional(),
                                fieldWithPath("noticeInfo.noticeCreatedAt").description("공지사항 생성일").optional(),
                                fieldWithPath("dailyDietInfo.dailyDietId").description("오늘의 식단 아이디").optional(),
                                fieldWithPath("dailyDietInfo.dailyDietImageResponse.id").description("오늘의 식단 이미지 아이디").optional(),
                                fieldWithPath("dailyDietInfo.dailyDietImageResponse.dailyDietType").description("MORNING, AFTERNOON, EVENING, MONTHLY;").optional(),
                                fieldWithPath("dailyDietInfo.dailyDietImageResponse.imageUrl").description("식단 이미지 URL").optional(),
                                fieldWithPath("dailyDietInfo.dailyDietImageResponse.createdAt").description("식단 이미지 생성일").optional(),
                                fieldWithPath("dailyShotInfo.dailyShotId").description("오늘의 한 컷 아이디").optional(),
                                fieldWithPath("dailyShotInfo.dailyShotContent").description("오늘의 한 컷 내용").optional(),
                                fieldWithPath("dailyShotInfo.dailyShotImageResponse.id").description("오늘의 한 컷 이미지 아이디").optional(),
                                fieldWithPath("dailyShotInfo.dailyShotImageResponse.imageUrl").description("오늘의 한 컷 이미지 URL").optional(),
                                fieldWithPath("dailyShotInfo.dailyShotImageResponse.createdAt").description("오오늘의 한 컷 이미지 생성일").optional(),
                                fieldWithPath("dailyMedicationInfo.dailyMedicationId").description("오늘의 복약 ID").optional(),
                                fieldWithPath("dailyMedicationInfo.morningContent").description("오전 투약 내용").optional(),
                                fieldWithPath("dailyMedicationInfo.afternoonContent").description("오후 투약 내용").optional(),
                                fieldWithPath("dailyMedicationInfo.nightContent").description("저녁 투약 내용").optional(),
                                fieldWithPath("dailyRehabilitationInfo.dailyRehabilitationId").description("오늘의 운동/재활 ID").optional(),
                                fieldWithPath("dailyRehabilitationInfo.treatment").description("재활 치료 내용").optional(),
                                fieldWithPath("dailyRehabilitationInfo.dailyExerciseResponses[].id").description("재활 운동 ID").optional(),
                                fieldWithPath("dailyRehabilitationInfo.dailyExerciseResponses[].content").description("재활 운동 내용").optional(),
                                fieldWithPath("dailyRehabilitationInfo.dailyExerciseResponses[].startTime").description("재활 운동 시작 시간").optional(),
                                fieldWithPath("dailyRehabilitationInfo.dailyExerciseResponses[].endTime").description("재활 운동 종료 시간").optional(),
                                fieldWithPath("communityScheduleResponses[].id").description("일정 ID").optional(),
                                fieldWithPath("communityScheduleResponses[].recordDate").description("일정 날짜").optional(),
                                fieldWithPath("communityScheduleResponses[].content").description("일정 내용").optional(),
                                fieldWithPath("communityManagerName").description("커뮤니티 관리자 이름").optional()
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }

    def "내가 속한 커뮤니티 목록확인"() {
        given:
        communityQueryService.myJoinedCommunity(_) >> List.of(new MyCommunityResponse(1L))

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/communities/me")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(document("community-mine",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].facilityId").type(JsonFieldType.NUMBER)
                                        .description("시설 아이디")
                        )
                ))
        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }


}
