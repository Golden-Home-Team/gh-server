package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.ElderlyFacilityResponse
import kr.co.goldenhome.service.ElderlyFacilityService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ElderlyFacilityControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    ElderlyFacilityService elderlyFacilityService = Mock()

    def "시설 상세조회 성공"() {
        given:
        def givenFacilityId = 1L
        def expectedResponse = new ElderlyFacilityResponse(givenFacilityId, "광진구", "더클래식500", "김수현", 760, 545, 216, 329, 71, 32, 39, "광진구 능동로 90", "02-2218-5692", "양로원")
        1 * elderlyFacilityService.read(givenFacilityId) >> expectedResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/facility/{facilityId}", givenFacilityId))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.id').value(expectedResponse.id())
            MockMvcResultMatchers.jsonPath('$.districtName').value(expectedResponse.districtName())
            MockMvcResultMatchers.jsonPath('$.name').value(expectedResponse.name())
            MockMvcResultMatchers.jsonPath('$.director').value(expectedResponse.director())
            MockMvcResultMatchers.jsonPath('$.capacity').value(expectedResponse.capacity())
            MockMvcResultMatchers.jsonPath('$.currentTotal').value(expectedResponse.currentTotal())
            MockMvcResultMatchers.jsonPath('$.currentMale').value(expectedResponse.currentMale())
            MockMvcResultMatchers.jsonPath('$.currentFemale').value(expectedResponse.currentFemale())
            MockMvcResultMatchers.jsonPath('$.staffTotal').value(expectedResponse.staffTotal())
            MockMvcResultMatchers.jsonPath('$.staffMale').value(expectedResponse.staffMale())
            MockMvcResultMatchers.jsonPath('$.staffFemale').value(expectedResponse.staffFemale())
            MockMvcResultMatchers.jsonPath('$.address').value(expectedResponse.address())
            MockMvcResultMatchers.jsonPath('$.phoneNumber').value(expectedResponse.phoneNumber())
            MockMvcResultMatchers.jsonPath('$.facilityType').value(expectedResponse.facilityType())
        }

    }
}
