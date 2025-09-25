package kr.co.goldenhome.docs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.FacilityApiResponse
import kr.co.goldenhome.enums.Gender
import kr.co.goldenhome.enums.HealthInsurance
import kr.co.goldenhome.enums.LongTermCareGrade
import kr.co.goldenhome.enums.PhysicalCondition
import kr.co.goldenhome.enums.Relationship
import kr.co.goldenhome.submission.dto.ResumeSubmissionResponse
import kr.co.goldenhome.entity.ResumeSubmission
import kr.co.goldenhome.enums.AdmissionStatus
import kr.co.goldenhome.submission.service.ResumeSubmissionService
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
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
        def expectedResponse = List.of(ResumeSubmissionResponse.of(
                ResumeSubmission.builder()
                        .id(1L)
                        .resumeId(1L)
                .facilityId(1L)
                .name("구준형")
                .dateOfBirth(LocalDate.of(2000,7,2))
                .gender(Gender.MALE)
                .longTermCareGrade(LongTermCareGrade.GRADE_1)
                .physicalCondition(PhysicalCondition.DEMENTIA)
                .healthInsurance(HealthInsurance.MEDICAL_AID_TYPE_1)
                .specialNotes("없음")
                .guardianName("구머니")
                .guardianContactInformation("01040363457")
                .relationship(Relationship.CHILD)
                .submitTime(LocalDateTime.of(2000, 7, 2, 12, 30))
                .admissionStatus(AdmissionStatus.PENDING_REVIEW)
                .otherRelationship("e.g. 기타 - 친구")
                 .build(),
                new FacilityApiResponse("", "")
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
                                fieldWithPath("[].physicalCondition").type(JsonFieldType.STRING)
                                        .description("HYPERTENSION, DIABETES, DEMENTIA, TRAUMA, ETC, NONE"),
                                fieldWithPath("[].healthInsurance").type(JsonFieldType.STRING)
                                        .description("NATIONAL(국민건강보험), MEDICAL_AID_TYPE_1(의료급여1종), MEDICAL_AID_TYPE_2(의료급여2종)"),
                                fieldWithPath("[].specialNotes").type(JsonFieldType.STRING)
                                        .description("특이사항"),
                                fieldWithPath("[].guardianName").type(JsonFieldType.STRING)
                                        .description("보호자 이름"),
                                fieldWithPath("[].guardianName").type(JsonFieldType.STRING)
                                        .description("보호자 이름"),
                                fieldWithPath("[].guardianContactInformation").type(JsonFieldType.STRING)
                                        .description("보호자 연락처"),
                                fieldWithPath("[].relationship").type(JsonFieldType.STRING)
                                        .description("CHILD, GRANDCHILD, SIBLING, ETC"),
                                fieldWithPath("[].submitTime").type(JsonFieldType.STRING)
                                        .description("제출시간"),
                                fieldWithPath("[].admissionStatus").type(JsonFieldType.STRING)
                                        .description("PENDING_REVIEW(열람 전), REVIEWED(열람 완료), IN_PROGRESS(심사 중), ELIGIBLE_FOR_ADMISSION(입소 가능), NOT_ELIGIBLE_FOR_ADMISSION(입소 불가), ADMITTED(입소 완료)"),
                                fieldWithPath("[].facilityName").type(JsonFieldType.STRING)
                                        .description("시설 이름"),
                                fieldWithPath("[].facilityAddress").type(JsonFieldType.STRING)
                                        .description("시설 주소"),
                                fieldWithPath("[].otherRelationship").type(JsonFieldType.STRING)
                                        .description("기타 관계일 시 추가설명")

                        )
                )
                )

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }

    }

    def "이력서 조회"() {
        given:
        def expectedResponse = ResumeSubmissionResponse.of(
                ResumeSubmission.builder()
                        .id(1L)
                        .resumeId(1L)
                        .facilityId(1L)
                        .name("구준형")
                        .dateOfBirth(LocalDate.of(2000,7,2))
                        .gender(Gender.MALE)
                        .longTermCareGrade(LongTermCareGrade.GRADE_1)
                        .physicalCondition(PhysicalCondition.DEMENTIA)
                        .healthInsurance(HealthInsurance.MEDICAL_AID_TYPE_1)
                        .specialNotes("없음")
                        .guardianName("구머니")
                        .guardianContactInformation("01040363457")
                        .relationship(Relationship.CHILD)
                        .submitTime(LocalDateTime.of(2000, 7, 2, 12, 30))
                        .admissionStatus(AdmissionStatus.PENDING_REVIEW)
                        .otherRelationship("기타 - 친구")
                        .build(),
                new FacilityApiResponse("", "")
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
                        fieldWithPath("physicalCondition").type(JsonFieldType.STRING)
                                .description("건강 상태"),
                        fieldWithPath("healthInsurance").type(JsonFieldType.STRING)
                                .description("NATIONAL(국민건강보험), MEDICAL_AID_TYPE_1(의료급여1종), MEDICAL_AID_TYPE_2(의료급여2종)"),
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
                        fieldWithPath("admissionStatus").type(JsonFieldType.STRING)
                                .description("평가상태"),
                        fieldWithPath("facilityName").type(JsonFieldType.STRING)
                                .description("시설 이름"),
                        fieldWithPath("facilityAddress").type(JsonFieldType.STRING)
                                .description("시설 주소"),
                        fieldWithPath("otherRelationship").type(JsonFieldType.STRING)
                                .description("기타 관계일 시 추가설명")

                )
        )
        )

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }

    }
}
