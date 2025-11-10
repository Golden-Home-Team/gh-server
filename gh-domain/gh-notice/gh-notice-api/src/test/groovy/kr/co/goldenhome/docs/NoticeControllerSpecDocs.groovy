package kr.co.goldenhome.docs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.auth.UserPrincipal
import kr.co.goldenhome.dto.NoticeRequest
import kr.co.goldenhome.entity.Notice
import kr.co.goldenhome.service.NoticeService
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class NoticeControllerSpecDocs extends Specification {


    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    NoticeService noticeService = Mock()

    def "공지등록"() {

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/notices")
                .principal(new UserPrincipal(1L))
        .content(objectMapper.writeValueAsString(new NoticeRequest("title", "content")))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("notice-write",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("제목(등록은 관리자만 가능)"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("내용")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공 여부")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }

    def "공지리스트조회"() {

        given:
        def givenLastId = 1L
        def givenPageSize = 20L
        1 * noticeService.readAll(givenLastId, givenPageSize) >> List.of(Notice.builder().id(1L).title("title").content("content").createdAt(LocalDateTime.of(2025,10,10,10,10)).updatedAt(LocalDateTime.of(2025,10,10,10,10)).build())

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/notices")
                .principal(new UserPrincipal(1L))
                .param("lastId", "1")
                .param("pageSize","20")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("notice-readAll",
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("lastId").description("이전 요청 시 마지막으로 조회한 공지 아이디"),
                                parameterWithName("pageSize").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                        .description("공지 아이디"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING)
                                        .description("공지 제목"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING)
                                        .description("공지 내용"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING)
                                        .description("공지 생성일"),
                                fieldWithPath("[].updatedAt").type(JsonFieldType.STRING)
                                        .description("공지 수정일")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }

    def "공지단일조회"() {

        given:
        def givenId = 1L
        1 * noticeService.read(givenId) >> Notice.builder().id(givenId).title("title").content("content").createdAt(LocalDateTime.of(2025,10,10,10,10)).updatedAt(LocalDateTime.of(2025,10,10,10,10)).build()

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/notices/{noticeId}", givenId)
                .principal(new UserPrincipal(1L))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("notice-read",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("공지 아이디"),
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("공지 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("공지 내용"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING)
                                        .description("공지 생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING)
                                        .description("공지 수정일")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }
}
