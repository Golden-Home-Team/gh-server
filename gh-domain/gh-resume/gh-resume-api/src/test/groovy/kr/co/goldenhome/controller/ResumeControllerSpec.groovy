package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.ResumeModifyRequest
import kr.co.goldenhome.dto.ResumeResponse
import kr.co.goldenhome.service.ResumeService
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

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ResumeControllerSpec extends Specification{

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
        resumeService.read(1L) >> expectedResponse

        when:
        def response = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/resumes")
        )

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

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value("true")
        }

    }
}
