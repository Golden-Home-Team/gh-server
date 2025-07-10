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

    def "시설 상세조회"() {
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

    def "시설 목록조회"() {
        given:
        def givenFacilityType = "양로원"
        def givenLastId = 1L
        def givenPageSize = 20L
        def expectedResponse = List.of(new ElderlyFacilityResponse(2L, "광진구", "더클래식500", "김수현", 760, 545, 216, 329, 71, 32, 39, "광진구 능동로 90", "02-2218-5692", "양로원"))
        1 * elderlyFacilityService.readAll(givenFacilityType, givenLastId, givenPageSize) >> expectedResponse

        when:
        def response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/facility/v1/readAll")
                .param("facilityType", givenFacilityType)
                .param("lastId", givenLastId.toString())
                .param("pageSize", givenPageSize.toString())
        )

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$[0].id').value(expectedResponse.get(0).id())
            MockMvcResultMatchers.jsonPath('$[0].districtName').value(expectedResponse.get(0).districtName())
            MockMvcResultMatchers.jsonPath('$[0].name').value(expectedResponse.get(0).districtName())
            MockMvcResultMatchers.jsonPath('$[0].director').value(expectedResponse.get(0).director())
            MockMvcResultMatchers.jsonPath('$[0].capacity').value(expectedResponse.get(0).capacity())
            MockMvcResultMatchers.jsonPath('$[0].currentTotal').value(expectedResponse.get(0).currentTotal())
            MockMvcResultMatchers.jsonPath('$[0].currentMale').value(expectedResponse.get(0).currentMale())
            MockMvcResultMatchers.jsonPath('$[0].currentFemale').value(expectedResponse.get(0).currentFemale())
            MockMvcResultMatchers.jsonPath('$[0].staffTotal').value(expectedResponse.get(0).staffTotal())
            MockMvcResultMatchers.jsonPath('$[0].staffMale').value(expectedResponse.get(0).staffMale())
            MockMvcResultMatchers.jsonPath('$[0].staffFemale').value(expectedResponse.get(0).staffFemale())
            MockMvcResultMatchers.jsonPath('$[0].address').value(expectedResponse.get(0).address())
            MockMvcResultMatchers.jsonPath('$[0].phoneNumber').value(expectedResponse.get(0).phoneNumber())
            MockMvcResultMatchers.jsonPath('$[0].facilityType').value(expectedResponse.get(0).facilityType())
        }


    }
}
