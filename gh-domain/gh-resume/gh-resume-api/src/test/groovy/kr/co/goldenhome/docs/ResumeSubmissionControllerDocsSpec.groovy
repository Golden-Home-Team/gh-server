package kr.co.goldenhome.docs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.ResumeSubmissionModifyRequest
import kr.co.goldenhome.dto.ResumeSubmissionResponse
import kr.co.goldenhome.entity.ResumeSubmission
import kr.co.goldenhome.enums.AdmissionStatus
import kr.co.goldenhome.service.ResumeSubmissionService
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
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ResumeSubmissionControllerDocsSpec extends Specification{

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    ResumeSubmissionService resumeSubmissionService = Mock()

    def "이력서 제출"() {
        given:

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/resumes-submission/{facilityId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("resume-submission-submit",
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
            MockMvcResultMatchers.jsonPath('$.success').value("true")
        }

    }

    def "이력서 목록조회"() {
        given:
        def givenLastId = 1L
        def givenPageSize = 20L
        def expectedResponse = List.of(ResumeSubmissionResponse.from(
                ResumeSubmission.builder()
                        .id(1L)
                        .resumeId(1L)
                .facilityId(1L)
                .name("구준형")
                .dateOfBirth(LocalDate.of(2000,7,2))
                .gender("남")
                .longTermCareGrade("A")
                .majorDiseases("허리통증")
                .specialNotes("없음")
                .guardianName("구머니")
                .guardianContactInformation("01040363457")
                .relationship("모")
                        .submitTime(LocalDateTime.of(2000, 7, 2, 12, 30))
                        .status(AdmissionStatus.PENDING_REVIEW)
                        .build()
            )
        )
        1 * resumeSubmissionService.readAll(*_) >> expectedResponse

        when:
        def response = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/resumes-submission/readAll")
                    .param("lastId", givenLastId.toString())
                    .param("pageSize",givenPageSize.toString())
        )
                .andDo(document("resume-submission-readAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("lastId").description("이전 요청 시 마지막으로 조회한 이력서제출 아이디"),
                                parameterWithName("pageSize").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                        .description("이력서제출 목록"),
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                        .description("이력서제출 아이디"),
                                fieldWithPath("[].resumeId").type(JsonFieldType.NUMBER)
                                        .description("이력서 아이디"),
                                fieldWithPath("[].facilityId").type(JsonFieldType.NUMBER)
                                        .description("시설 아이디"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("[].dateOfBirth").type(JsonFieldType.STRING)
                                        .description("생년월일 e.g. 2000-07-02"),
                                fieldWithPath("[].gender").type(JsonFieldType.STRING)
                                        .description("성별"),
                                fieldWithPath("[].longTermCareGrade").type(JsonFieldType.STRING)
                                        .description("장기요양등급"),
                                fieldWithPath("[].majorDiseases").type(JsonFieldType.STRING)
                                        .description("주요질병"),
                                fieldWithPath("[].specialNotes").type(JsonFieldType.STRING)
                                        .description("특이사항"),
                                fieldWithPath("[].guardianName").type(JsonFieldType.STRING)
                                        .description("보호자 이름"),
                                fieldWithPath("[].guardianName").type(JsonFieldType.STRING)
                                        .description("보호자 이름"),
                                fieldWithPath("[].guardianContactInformation").type(JsonFieldType.STRING)
                                        .description("보호자 연락처"),
                                fieldWithPath("[].relationship").type(JsonFieldType.STRING)
                                        .description("보호자 관계"),
                                fieldWithPath("[].submitTime").type(JsonFieldType.STRING)
                                        .description("제출시간"),
                                fieldWithPath("[].status").type(JsonFieldType.STRING)
                                        .description("평가상태")

                        )
                )
                )

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$[0].id').value(1L)
            MockMvcResultMatchers.jsonPath('$[0].resumeId').value(1L)
            MockMvcResultMatchers.jsonPath('$[0].facilityId').value(1L)
            MockMvcResultMatchers.jsonPath('$[0].name').value("구준형")
            MockMvcResultMatchers.jsonPath('$[0].dateOfBirth').value("2000-07-02")
            MockMvcResultMatchers.jsonPath('$[0].gender').value("남")
            MockMvcResultMatchers.jsonPath('$[0].longTermCareGrade').value("B")
            MockMvcResultMatchers.jsonPath('$[0].majorDiseases').value("허리통증")
            MockMvcResultMatchers.jsonPath('$[0].specialNotes').value("없음")
            MockMvcResultMatchers.jsonPath('$[0].guardianName').value("구머니")
            MockMvcResultMatchers.jsonPath('$[0].guardianContactInformation').value("01040363457")
            MockMvcResultMatchers.jsonPath('$[0].relationship').value("모")
            MockMvcResultMatchers.jsonPath('$[0].submitTime').value("제출시간")
            MockMvcResultMatchers.jsonPath('$[0].status').value("평가상태")
        }

    }

    def "이력서 조회"() {
        given:
        def expectedResponse = ResumeSubmissionResponse.from(
                ResumeSubmission.builder()
                        .id(1L)
                        .resumeId(1L)
                        .facilityId(1L)
                        .name("구준형")
                        .dateOfBirth(LocalDate.of(2000,7,2))
                        .gender("남")
                        .longTermCareGrade("A")
                        .majorDiseases("허리통증")
                        .specialNotes("없음")
                        .guardianName("구머니")
                        .guardianContactInformation("01040363457")
                        .relationship("모")
                        .submitTime(LocalDateTime.of(2000, 7, 2, 12, 30))
                        .status(AdmissionStatus.PENDING_REVIEW)
                        .build()
        )

        1 * resumeSubmissionService.read(*_) >> expectedResponse

        when:
        def response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/resumes-submission/{id}", 1L)
        ).andDo(document("resume-submission-read",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("id").description("이력서제출 아이디")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                                .description("이력서제출 아이디"),
                        fieldWithPath("resumeId").type(JsonFieldType.NUMBER)
                                .description("이력서 아이디"),
                        fieldWithPath("facilityId").type(JsonFieldType.NUMBER)
                                .description("시설 아이디"),
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
                        fieldWithPath("relationship").type(JsonFieldType.STRING)
                                .description("보호자 관계"),
                        fieldWithPath("submitTime").type(JsonFieldType.STRING)
                                .description("제출시간"),
                        fieldWithPath("status").type(JsonFieldType.STRING)
                                .description("평가상태")

                )
        )
        )

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.id').value(1L)
            MockMvcResultMatchers.jsonPath('$.resumeId').value(1L)
            MockMvcResultMatchers.jsonPath('$.facilityId').value(1L)
            MockMvcResultMatchers.jsonPath('$.name').value("구준형")
            MockMvcResultMatchers.jsonPath('$.dateOfBirth').value("2000-07-02")
            MockMvcResultMatchers.jsonPath('$.gender').value("남")
            MockMvcResultMatchers.jsonPath('$.longTermCareGrade').value("B")
            MockMvcResultMatchers.jsonPath('$.majorDiseases').value("허리통증")
            MockMvcResultMatchers.jsonPath('$.specialNotes').value("없음")
            MockMvcResultMatchers.jsonPath('$.guardianName').value("구머니")
            MockMvcResultMatchers.jsonPath('$.guardianContactInformation').value("01040363457")
            MockMvcResultMatchers.jsonPath('$.relationship').value("모")
            MockMvcResultMatchers.jsonPath('$.submitTime').value("제출시간")
            MockMvcResultMatchers.jsonPath('$.status').value("평가상태")
        }

    }

    def "이력서 수정"() {
        given:
        def givenDateOfBirth = LocalDate.of(2000, 7, 2)
        def request = new ResumeSubmissionModifyRequest(
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
        def response = mockMvc.perform(MockMvcRequestBuilders.patch("/api/resumes-submission/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(document("resume-submission-modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("이력서제출 아이디")
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
