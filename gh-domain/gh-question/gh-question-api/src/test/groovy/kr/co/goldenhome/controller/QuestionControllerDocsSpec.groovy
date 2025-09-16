package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.QuestionDomainOptionInnerResponse
import kr.co.goldenhome.dto.QuestionResponse
import kr.co.goldenhome.dto.QuestionSurveyRequest
import kr.co.goldenhome.dto.QuestionSurveyResponse
import kr.co.goldenhome.service.QuestionService
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class QuestionControllerDocsSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    QuestionService questionService = Mock()

    def "설문지조회"() {

        given:
        questionService.readAll() >> List.of(new QuestionResponse(
                1L,
                "옷벗고 입기가 가능하십니까?",
                List.of(new QuestionDomainOptionInnerResponse(
                        1L,
                        "완전자립",
                        1
                ))
        ))

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/questions")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("question-readAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].questionDomainId").type(JsonFieldType.NUMBER)
                                        .description("질문 영역(신체기능, 인지기능...) 아이디 "),
                                fieldWithPath("[].content").type(JsonFieldType.STRING)
                                        .description("질문 내용"),
                                fieldWithPath("[].questionDomainOptionInnerResponses[]").type(JsonFieldType.ARRAY)
                                        .description("질문의 답변 선택지 목록"),
                                fieldWithPath("[].questionDomainOptionInnerResponses[].id").type(JsonFieldType.NUMBER)
                                        .description("답변 선택지 아이디"),
                                fieldWithPath("[].questionDomainOptionInnerResponses[].name").type(JsonFieldType.STRING)
                                        .description("답변 선택지 이름"),
                                fieldWithPath("[].questionDomainOptionInnerResponses[].originalScore").type(JsonFieldType.NUMBER)
                                        .description("답변 선택지 점수 가중치"),


                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value("true")
        }
    }

    def "설문지 작성"() {
        given:
        def request = new QuestionSurveyRequest(
                List.of(1L)
        )
        def expectedResponse = new QuestionSurveyResponse("1등급", 42.0)
        questionService.survey(request) >> expectedResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/questions/survey")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(document("question-survey",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("questionDomainOptionIds").type(JsonFieldType.ARRAY)
                                        .description("답변 옵션 아이디 리스트 - " +
                                                "클라언트가 선택한 답변 선택지 아이디값을 보내주시면 됩니다."),
                        ),
                        responseFields(
                                fieldWithPath("grade").type(JsonFieldType.STRING)
                                        .description("N등급"),
                                fieldWithPath("finalScore").type(JsonFieldType.NUMBER)
                                        .description("최종점수"),
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value("true")
        }

    }
}
