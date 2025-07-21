package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.submission.dto.ResumeSubmissionModifyRequest
import kr.co.goldenhome.submission.dto.ResumeSubmissionResponse
import kr.co.goldenhome.entity.ResumeSubmission
import kr.co.goldenhome.enums.AdmissionStatus
import kr.co.goldenhome.submission.service.ResumeSubmissionService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ResumeSubmissionControllerSpec extends Specification{

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

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value("true")
        }

    }

    def "이력서 목록조회"() {
        given:
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
            MockMvcResultMatchers.jsonPath('$[0].relationShip').value("모")
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
                        .build()
        )

        1 * resumeSubmissionService.read(*_) >> expectedResponse

        when:
        def response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/resumes-submission/{id}", 1L)
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
            MockMvcResultMatchers.jsonPath('$.relationShip').value("모")
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
        def response = mockMvc.perform(MockMvcRequestBuilders.patch("/api/resumes-submission")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value("true")
        }

    }
}
