package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.auth.UserPrincipal
import kr.co.goldenhome.dto.CommunityInquiryRequest
import kr.co.goldenhome.entity.CommunityInquiry
import kr.co.goldenhome.enums.CommunityInquiryStatus
import kr.co.goldenhome.enums.CommunityInquiryType
import kr.co.goldenhome.service.CommunityInquiryService
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
class CommunityInquiryControllerSpecDocs extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    CommunityInquiryService communityInquiryService = Mock()

    def "요청사항 작성"() {
        given:
        def givenFacilityId = 1L
        def givenRequest = new CommunityInquiryRequest(LocalDate.now(), "요청사항입니다.", CommunityInquiryType.DIET.name(), false)

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/communities/{facilityId}/inquiry", givenFacilityId)
                .principal(new UserPrincipal(1L))

                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest))
        )

                .andDo(document("community-inquiry-write",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        requestFields(
                                fieldWithPath("recordDate").description("기록일 e.g. 2025-08-19").type(JsonFieldType.STRING),
                                fieldWithPath("content").description("내용").type(JsonFieldType.STRING),
                                fieldWithPath("type").description("DIET(식사), MEDICATION(복약/건강), REHABILITATION(운동/재활), VISIT(방문/외출), OTHER(기타)").type(JsonFieldType.STRING),
                                fieldWithPath("isUrgent").description("긴급 여부").type(JsonFieldType.BOOLEAN)
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

    def "요청사항 조회"() {
        given:
        def givenFacilityId = 1L
        def givenInquiryId = 2L
        def givenResponse = CommunityInquiry.builder().id(1L).facilityId(givenFacilityId).content("밥 많이 주세요").recordDate(LocalDate.now()).userId(1L).status(CommunityInquiryStatus.PENDING).type(CommunityInquiryType.DIET).isUrgent(false).build()
        communityInquiryService.read(givenInquiryId) >> givenResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/communities/{facilityId}/inquiry/{inquiryId}", givenFacilityId, givenInquiryId)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(document("community-inquiry-read",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디"),
                                parameterWithName("inquiryId").description("시설 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("요청사항 아이디"),
                                fieldWithPath("facilityId").type(JsonFieldType.NUMBER)
                                        .description("시설 아이디"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                        .description("작성자 아이디"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("본문"),
                                fieldWithPath("recordDate").type(JsonFieldType.STRING)
                                        .description("기록일"),
                                fieldWithPath("isUrgent").type(JsonFieldType.BOOLEAN)
                                        .description("긴급여부"),
                                fieldWithPath("type").type(JsonFieldType.STRING)
                                        .description("DIET(식사), MEDICATION(복약/건강), REHABILITATION(운동/재활), VISIT(방문/외출), OTHER(기타)"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("PENDING, COMPLETED")

                        )
                ))
        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }

    def "요청사항 목록조회"() {
        given:
        def givenFacilityId = 1L
        def givenResponse = List.of(CommunityInquiry.builder().id(1L).facilityId(givenFacilityId).content("밥 많이 주세요").recordDate(LocalDate.now()).userId(1L).status(CommunityInquiryStatus.PENDING).type(CommunityInquiryType.DIET).isUrgent(false).build())
        communityInquiryService.readAll(*_) >> givenResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/communities/{facilityId}/inquiry", givenFacilityId)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("lastId", "1")
                .queryParam("pageSize", "20")
        )
                .andDo(document("community-inquiry-readAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        queryParameters(
                                parameterWithName("lastId").description("마지막으로 조회한 요청사항 아이디"),
                                parameterWithName("pageSize").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                        .description("요청사항 아이디"),
                                fieldWithPath("[].facilityId").type(JsonFieldType.NUMBER)
                                        .description("시설 아이디"),
                                fieldWithPath("[].userId").type(JsonFieldType.NUMBER)
                                        .description("작성자 아이디"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING)
                                        .description("본문 (50자 잘라서 응답)"),
                                fieldWithPath("[].recordDate").type(JsonFieldType.STRING)
                                        .description("기록일"),
                                fieldWithPath("[].isUrgent").type(JsonFieldType.BOOLEAN)
                                        .description("긴급여부"),
                                fieldWithPath("[].type").type(JsonFieldType.STRING)
                                        .description("DIET(식사), MEDICATION(복약/건강), REHABILITATION(운동/재활), VISIT(방문/외출), OTHER(기타)"),
                                fieldWithPath("[].status").type(JsonFieldType.STRING)
                                        .description("PENDING, COMPLETED")

                        )
                ))
        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }
}
