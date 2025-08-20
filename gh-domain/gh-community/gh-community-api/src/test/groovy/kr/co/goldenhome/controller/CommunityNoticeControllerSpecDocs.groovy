package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.CommunityNoticeRequest
import kr.co.goldenhome.dto.CommunityNoticeUpdateRequest
import kr.co.goldenhome.entity.CommunityNotice
import kr.co.goldenhome.service.CommunityNoticeService
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

import java.time.LocalDateTime

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
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
class CommunityNoticeControllerSpecDocs extends Specification{

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    CommunityNoticeService communityNoticeService = Mock()

    def "공지 작성"() {
        given:
        def givenFacilityId = 1L
        def givenRequest = new CommunityNoticeRequest("제목", "내용")

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/communities/{facilityId}/notice", givenFacilityId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest))
        )
                .andDo(document("community-notice-write",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
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

    def "공지 수정"() {
        given:
        def givenFacilityId = 1L
        def givenNoticeId = 1L
        def givenRequest = new CommunityNoticeUpdateRequest("제목", "내용")

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.put("/api/communities/{facilityId}/notice/{noticeId}", givenFacilityId, givenNoticeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest))
        )
                .andDo(document("community-notice-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디"),
                                parameterWithName("noticeId").description("공지 아이디")
                        ),
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
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

    def "공지 조회"() {
        given:
        def givenFacilityId = 1L
        def givenNoticeId = 1L
        communityNoticeService.read(givenNoticeId) >> CommunityNotice.builder().id(1L).title("제목").content("내용").createdAt(LocalDateTime.of(2025, 10, 10, 10, 10)).build()

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/communities/{facilityId}/notice/{noticeId}", givenFacilityId, givenNoticeId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("community-notice-read",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디"),
                                parameterWithName("noticeId").description("공지 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("공지 아이디"),
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("공지 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("공지 내용"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING)
                                        .description("공지 작성일")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.id').value(1L)
            MockMvcResultMatchers.jsonPath('$.title').value("제목")
            MockMvcResultMatchers.jsonPath('$.content').value("내용")
            MockMvcResultMatchers.jsonPath('$.createdAt').value(LocalDateTime.of(2025, 10, 10, 10, 10))
        }
    }

    def "공지 리스트 조회"() {
        given:
        def givenFacilityId = 1L
        communityNoticeService.readAll(givenFacilityId, 1L, 20L) >> List.of(CommunityNotice.builder().id(1L).title("제목").content("내용").createdAt(LocalDateTime.of(2025, 10, 10, 10, 10)).build())

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/communities/{facilityId}/notice", givenFacilityId)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("lastId", "1")
                .queryParam("pageSize", "20")
        )
                .andDo(document("community-notice-readAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        queryParameters(
                                parameterWithName("lastId").description("마지막으로 조회한 공지 아이디 (필수 x)"),
                                parameterWithName("pageSize").description("페이지 사이즈 default 20")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                        .description("공지 아이디"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING)
                                        .description("공지 제목"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING)
                                        .description("공지 내용"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING)
                                        .description("공지 작성일"),
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$[0].id').value(1L)
            MockMvcResultMatchers.jsonPath('$[0].title').value("제목")
            MockMvcResultMatchers.jsonPath('$[0].content').value("내용")
            MockMvcResultMatchers.jsonPath('$[0].createdAt').value(LocalDateTime.of(2025, 10, 10, 10, 10))
        }
    }
}
