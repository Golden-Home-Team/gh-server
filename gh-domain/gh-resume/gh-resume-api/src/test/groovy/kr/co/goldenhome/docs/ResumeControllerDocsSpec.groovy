package kr.co.goldenhome.docs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.auth.UserPrincipal
import kr.co.goldenhome.enums.AdmissionTimeFrame
import kr.co.goldenhome.enums.Gender
import kr.co.goldenhome.enums.HealthInsurance
import kr.co.goldenhome.enums.LongTermCareGrade
import kr.co.goldenhome.enums.PhysicalCondition
import kr.co.goldenhome.enums.Relationship
import kr.co.goldenhome.resume.dto.ResumeModifyRequest
import kr.co.goldenhome.resume.dto.ResumeResponse
import kr.co.goldenhome.resume.service.ResumeService
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
                Gender.MALE.name(),
                PhysicalCondition.DEMENTIA.name(),
                LongTermCareGrade.GRADE_1.name(),
                HealthInsurance.MEDICAL_AID_TYPE_1.name(),
                "없음",
                "구머니",
                "01040363457",
                Relationship.CHILD.name(),
                "양로원",
                AdmissionTimeFrame.IMMEDIATELY.name(),
                "기타 - 친구"
        )

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/resumes")
                .principal(new UserPrincipal(1L))
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
                                        .description("MALE, FEMALE"),
                                fieldWithPath("longTermCareGrade").type(JsonFieldType.STRING)
                                        .description("GRADE_1, GRADE_2, GRADE_3, GRADE_4, GRADE_5, GRADE_6, IN_PROGRESS, NO_GRADE"),
                                fieldWithPath("physicalCondition").type(JsonFieldType.STRING)
                                        .description("HYPERTENSION(고혈압), DIABETES(당뇨), DEMENTIA(치매), TRAUMA(외상 상태), ETC(기타), NONE"),
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
                                        .description("CHILD, GRANDCHILD, SIBLING, ETC"),
                                fieldWithPath("facilityType").type(JsonFieldType.STRING)
                                        .description("시설 타입 e.g. 양로원"),
                                fieldWithPath("admissionTimeFrame").type(JsonFieldType.STRING)
                                        .description("IMMEDIATELY, WITHIN_1_MONTH, WITHIN_3_MONTHS, TO_BE_DETERMINED"),
                                fieldWithPath("otherRelationship").type(JsonFieldType.STRING)
                                        .description("보호자와 관계가 기타일 때 입력해주세요")

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
                Gender.MALE,
                PhysicalCondition.DEMENTIA,
                LongTermCareGrade.GRADE_2,
                HealthInsurance.MEDICAL_AID_TYPE_1,
                "없음",
                "구머니",
                "01040363457",
                Relationship.CHILD,
                "양로원",
                LocalDateTime.of(2020, 10, 10, 10, 10),
                AdmissionTimeFrame.IMMEDIATELY,
                "기타 :..."
        )
        resumeService.read(*_) >> expectedResponse

        when:
        def response = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/resumes")
                    .principal(new UserPrincipal(1L)))

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
                                        .description("MALE, FEMALE"),
                                fieldWithPath("longTermCareGrade").type(JsonFieldType.STRING)
                                        .description("GRADE_1, GRADE_2, GRADE_3, GRADE_4, GRADE_5, GRADE_6, IN_PROGRESS, NO_GRADE"),
                                fieldWithPath("physicalCondition").type(JsonFieldType.STRING)
                                        .description("HYPERTENSION, DIABETES, DEMENTIA, TRAUMA, ETC, NONE"),
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
                                        .description("CHILD, GRANDCHILD, SIBLING, ETC"),
                                fieldWithPath("facilityType").type(JsonFieldType.STRING)
                                        .description("시설 타입"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING)
                                        .description("수정 일"),
                                fieldWithPath("admissionTimeFrame").type(JsonFieldType.STRING)
                                        .description("IMMEDIATELY, WITHIN_1_MONTH, WITHIN_3_MONTHS, TO_BE_DETERMINED"),
                                fieldWithPath("otherRelationship").type(JsonFieldType.STRING)
                                        .description("보호자와 관계가 기타일 때 입력해주세요")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }

    }

    def "이력서 수정"() {
        given:
        def givenDateOfBirth = LocalDate.of(2000, 7, 2)
        def request = new ResumeModifyRequest(
                "구준형",
                givenDateOfBirth,
                Gender.MALE.name(),
                PhysicalCondition.DEMENTIA.name(),
                LongTermCareGrade.GRADE_1.name(),
                HealthInsurance.MEDICAL_AID_TYPE_1.name(),
                "없음",
                "구머니",
                "01040363457",
                Relationship.CHILD.name(),
                "양로원",
                AdmissionTimeFrame.IMMEDIATELY.name(),
                ""
        )

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.put("/api/resumes")
                .principal(new UserPrincipal(1L))
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
                                        .description("MALE, FEMALE"),
                                fieldWithPath("longTermCareGrade").type(JsonFieldType.STRING)
                                        .description("GRADE_1, GRADE_2, GRADE_3, GRADE_4, GRADE_5, GRADE_6, IN_PROGRESS, NO_GRADE"),
                                fieldWithPath("physicalCondition").type(JsonFieldType.STRING)
                                        .description("HYPERTENSION, DIABETES, DEMENTIA, TRAUMA, ETC, NONE"),
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
                                        .description("CHILD, GRANDCHILD, SIBLING, ETC"),
                                fieldWithPath("facilityType").type(JsonFieldType.STRING)
                                        .description("시설 타입 e.g. 양로원"),
                                fieldWithPath("admissionTimeFrame").type(JsonFieldType.STRING)
                                        .description("IMMEDIATELY, WITHIN_1_MONTH, WITHIN_3_MONTHS, TO_BE_DETERMINED"),
                                fieldWithPath("otherRelationship").type(JsonFieldType.STRING)
                                        .description("보호자와 관계가 기타일 때 입력해주세요")
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
