package kr.co.goldenhome.docs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.ResumeModifyRequest
import kr.co.goldenhome.dto.ResumeResponse
import kr.co.goldenhome.service.ResumeService
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
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs

class ResumeControllerDocsSpec extends Specification{

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    ResumeService resumeService = Mock()

    def "이력서 작성"() {
        given:
        def givenDateOfBirth = LocalDate.of(2000, 7, 2)
        def request = new ResumeModifyRequest(
                "구준형",
                givenDateOfBirth,
                "남",
                "B",
                "허리통증",
                "없음",
                "구머니",
                "01040363457",
                "모"
        )

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(document("resume-write",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("dateOfBirth").type(JsonFieldType.STRING)
                                        .description("생년월일 e.g. 2000-07-02"),
                                fieldWithPath("gender").type(JsonFieldType.STRING)
                                        .description("성별"),
                                fieldWithPath("longTermCareGrade").type(JsonFieldType.STRING)
                                        .description("장기요양등급"),
                                fieldWithPath("majorDiseases").type(JsonFieldType.STRING)
                                        .description("주요질병"),
                                fieldWithPath("specialNotes").type(JsonFieldType.STRING)
                                        .description("특이사항"),
                                fieldWithPath("guardianName").type(JsonFieldType.STRING)
                                        .description("보호자 이름"),
                                fieldWithPath("guardianName").type(JsonFieldType.STRING)
                                        .description("보호자 이름"),
                                fieldWithPath("guardianContactInformation").type(JsonFieldType.STRING)
                                        .description("보호자 연락처"),
                                fieldWithPath("relationShip").type(JsonFieldType.STRING)
                                        .description("보호자 관계")
                        ),
                        responseFields(

                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value("true")
        }

    }

    def "이력서 확인"() {
        given:
        def expectedResponse = new ResumeResponse(
                1L,
                1L,
                "구준형",
                LocalDate.of(2000,7,2),
                "남",
                "B",
                "허리통증",
                "없음",
                "구머니",
                "01040363457",
                "모"
        )
        resumeService.read(*_) >> expectedResponse

        when:
        def response = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/resumes"))
                .andDo(document("resume-read",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("이력서 id"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                        .description("사용자 id"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("dateOfBirth").type(JsonFieldType.STRING)
                                        .description("생년월일 e.g. 2000-07-02"),
                                fieldWithPath("gender").type(JsonFieldType.STRING)
                                        .description("성별"),
                                fieldWithPath("longTermCareGrade").type(JsonFieldType.STRING)
                                        .description("장기요양등급"),
                                fieldWithPath("majorDiseases").type(JsonFieldType.STRING)
                                        .description("주요질병"),
                                fieldWithPath("specialNotes").type(JsonFieldType.STRING)
                                        .description("특이사항"),
                                fieldWithPath("guardianName").type(JsonFieldType.STRING)
                                        .description("보호자 이름"),
                                fieldWithPath("guardianName").type(JsonFieldType.STRING)
                                        .description("보호자 이름"),
                                fieldWithPath("guardianContactInformation").type(JsonFieldType.STRING)
                                        .description("보호자 연락처"),
                                fieldWithPath("relationShip").type(JsonFieldType.STRING)
                                        .description("보호자 관계")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.id').value(1L)
            MockMvcResultMatchers.jsonPath('$.userId').value(1L)
            MockMvcResultMatchers.jsonPath('$.name').value("남")
            MockMvcResultMatchers.jsonPath('$.dateOfBirth').value("2000-07-02")
            MockMvcResultMatchers.jsonPath('$.gender').value("남")
            MockMvcResultMatchers.jsonPath('$.longTermCareGrade').value("B")
            MockMvcResultMatchers.jsonPath('$.majorDiseases').value("허리통증")
            MockMvcResultMatchers.jsonPath('$.specialNotes').value("없음")
            MockMvcResultMatchers.jsonPath('$.guardianName').value("구머니")
            MockMvcResultMatchers.jsonPath('$.guardianContactInformation').value("01040363457")
            MockMvcResultMatchers.jsonPath('$.relationShip').value("모")
        }

    }

    def "이력서 수정"() {
        given:
        def givenDateOfBirth = LocalDate.of(2000, 7, 2)
        def request = new ResumeModifyRequest(
                "구준형",
                givenDateOfBirth,
                "남",
                "B",
                "허리통증",
                "없음",
                "구머니",
                "01040363457",
                "모"
        )

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.patch("/api/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(document("resume-modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("dateOfBirth").type(JsonFieldType.STRING)
                                        .description("생년월일 e.g. 2000-07-02"),
                                fieldWithPath("gender").type(JsonFieldType.STRING)
                                        .description("성별"),
                                fieldWithPath("longTermCareGrade").type(JsonFieldType.STRING)
                                        .description("장기요양등급"),
                                fieldWithPath("majorDiseases").type(JsonFieldType.STRING)
                                        .description("주요질병"),
                                fieldWithPath("specialNotes").type(JsonFieldType.STRING)
                                        .description("특이사항"),
                                fieldWithPath("guardianName").type(JsonFieldType.STRING)
                                        .description("보호자 이름"),
                                fieldWithPath("guardianName").type(JsonFieldType.STRING)
                                        .description("보호자 이름"),
                                fieldWithPath("guardianContactInformation").type(JsonFieldType.STRING)
                                        .description("보호자 연락처"),
                                fieldWithPath("relationShip").type(JsonFieldType.STRING)
                                        .description("보호자 관계")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value("true")
        }

    }
}
