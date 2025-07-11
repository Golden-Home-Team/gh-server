package kr.co.goldenhome.docs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.ElderlyFacilityResponse
import kr.co.goldenhome.service.ElderlyFacilityService

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*
import static org.springframework.restdocs.payload.PayloadDocumentation.*
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ElderlyFacilityControllerDocsSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    ElderlyFacilityService elderlyFacilityService = Mock()

    def "시설 상세조회 성공"() {
        given:
        def givenFacilityId = 1L
        def expectedResponse = new ElderlyFacilityResponse(givenFacilityId, "광진구", "더클래식500", "김수현", 760, 545, 216, 329, 71, 32, 39, "광진구 능동로 90", "02-2218-5692", "양로원", "A", 15000, 2023)
        1 * elderlyFacilityService.read(givenFacilityId) >> expectedResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/facility/{facilityId}", givenFacilityId))
                .andDo(document("facility-read",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("시설 아이디"),
                                fieldWithPath("districtName").type(JsonFieldType.STRING)
                                        .description("시/군/구"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("시설명"),
                                fieldWithPath("director").type(JsonFieldType.STRING)
                                        .description("관리자명"),
                                fieldWithPath("capacity").type(JsonFieldType.NUMBER)
                                        .description("정원"),
                                fieldWithPath("currentTotal").type(JsonFieldType.NUMBER)
                                        .description("현원 - 계"),
                                fieldWithPath("currentMale").type(JsonFieldType.NUMBER)
                                        .description("현원 - 남"),
                                fieldWithPath("currentFemale").type(JsonFieldType.NUMBER)
                                        .description("현원 - 여"),
                                fieldWithPath("staffTotal").type(JsonFieldType.NUMBER)
                                        .description("종사자수 - 계"),
                                fieldWithPath("staffMale").type(JsonFieldType.NUMBER)
                                        .description("종사자수 - 남"),
                                fieldWithPath("staffFemale").type(JsonFieldType.NUMBER)
                                        .description("종사자수 - 여"),
                                fieldWithPath("address").type(JsonFieldType.STRING)
                                        .description("소재지"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                                        .description("전화번호"),
                                fieldWithPath("facilityType").type(JsonFieldType.STRING)
                                        .description("시설 종류"),
                                fieldWithPath("grade").type(JsonFieldType.STRING)
                                        .description("시설 등급"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                        .description("가격"),
                                fieldWithPath("establishmentYear").type(JsonFieldType.NUMBER)
                                        .description("설립연도")

                        )
                )
                )

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
            MockMvcResultMatchers.jsonPath('$.grade').value(expectedResponse.grade())
            MockMvcResultMatchers.jsonPath('$.price').value(expectedResponse.price())
            MockMvcResultMatchers.jsonPath('$.establishmentYear').value(expectedResponse.establishmentYear())
        }

    }

    def "시설 목록조회 - deprecated"() {
        given:
        def givenFacilityType = "양로원"
        def givenLastId = 1L
        def givenPageSize = 20L
        def expectedResponse = List.of(new ElderlyFacilityResponse(1L, "광진구", "더클래식500", "김수현", 760, 545, 216, 329, 71, 32, 39, "광진구 능동로 90", "02-2218-5692", "양로원", "A", 15000, 2023))
        1 * elderlyFacilityService.readAll(givenFacilityType, givenLastId, givenPageSize) >> expectedResponse

        when:
        def response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/facility/v1/readAll")
                        .param("facilityType", givenFacilityType)
                        .param("lastId", givenLastId.toString())
                        .param("pageSize", givenPageSize.toString())
        ).andDo(document("facility-readAll",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("facilityType").description("시설종류 e.g. 양로원, 요양원, 단기보호, 방문간호, 방문요양, 방문목욕, 주야간보호"),
                        parameterWithName("lastId").description("이전 요청 시 마지막으로 조회한 시설 아이디"),
                        parameterWithName("pageSize").description("페이지 크기")
                ),
                responseFields(
                        fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                .description("시설 목록"),
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                .description("시설 아이디"),
                        fieldWithPath("[].districtName").type(JsonFieldType.STRING)
                                .description("시/군/구"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING)
                                .description("시설명"),
                        fieldWithPath("[].director").type(JsonFieldType.STRING)
                                .description("관리자명"),
                        fieldWithPath("[].capacity").type(JsonFieldType.NUMBER)
                                .description("정원"),
                        fieldWithPath("[].currentTotal").type(JsonFieldType.NUMBER)
                                .description("현원 - 계"),
                        fieldWithPath("[].currentMale").type(JsonFieldType.NUMBER)
                                .description("현원 - 남"),
                        fieldWithPath("[].currentFemale").type(JsonFieldType.NUMBER)
                                .description("현원 - 여"),
                        fieldWithPath("[].staffTotal").type(JsonFieldType.NUMBER)
                                .description("종사자수 - 계"),
                        fieldWithPath("[].staffMale").type(JsonFieldType.NUMBER)
                                .description("종사자수 - 남"),
                        fieldWithPath("[].staffFemale").type(JsonFieldType.NUMBER)
                                .description("종사자수 - 여"),
                        fieldWithPath("[].address").type(JsonFieldType.STRING)
                                .description("소재지"),
                        fieldWithPath("[].phoneNumber").type(JsonFieldType.STRING)
                                .description("전화번호"),
                        fieldWithPath("[].facilityType").type(JsonFieldType.STRING)
                                .description("시설 종류"),
                        fieldWithPath("[].grade").type(JsonFieldType.STRING)
                                .description("시설 등급"),
                        fieldWithPath("[].price").type(JsonFieldType.NUMBER)
                                .description("시설 종류"),
                        fieldWithPath("[].establishmentYear").type(JsonFieldType.NUMBER)
                                .description("시설연도")

                )
        ))

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

    def "시설 목록조회"() {
        given:
        def givenQuery = "더클래식"
        def givenAddress = "광진구"
        def givenFacilityType = "양로원"
        def givenGrade = "A"
        def givenMinPrice = 0
        def givenMaxPrice = 15000
        def givenWithinYears = 1
        def givenPage = 1
        def givenSize = 20
        def expectedResponse = List.of(new ElderlyFacilityResponse(1L, "광진구", "더클래식500", "김수현", 760, 545, 216, 329, 71, 32, 39, "광진구 능동로 90", "02-2218-5692", "양로원", "A", 15000, 2023))

        1 * elderlyFacilityService.search(givenQuery, givenAddress, givenFacilityType, givenGrade,
                givenMinPrice, givenMaxPrice, givenWithinYears, givenPage, givenSize
        ) >> expectedResponse

        when:
        def response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/facility/v2/readAll")
                        .param("query", givenQuery)
                        .param("address", givenAddress)
                        .param("facilityType", givenFacilityType)
                        .param("grade", givenGrade)
                        .param("minPrice", givenMinPrice as String)
                        .param("maxPrice", givenMaxPrice as String)
                        .param("withinYears", givenWithinYears as String)
                        .param("page", givenPage as String)
                        .param("size", givenSize as String)
        ).andDo(document("facility-search",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("query").description("시설명 검색"),
                        parameterWithName("address").description("시설주소 검색"),
                        parameterWithName("facilityType").description("시설종류 e.g. 양로원, 요양원, 단기보호, 방문간호, 방문요양, 방문목욕, 주야간보호"),
                        parameterWithName("grade").description("시설등급"),
                        parameterWithName("minPrice").description("최소가격 default = 0"),
                        parameterWithName("maxPrice").description("최대가격 default = 100000000"),
                        parameterWithName("withinYears").description("설립연도 n년 이내 e.g. 1"),
                        parameterWithName("page").description("페이지 default = 1"),
                        parameterWithName("size").description("페이지 크기 default = 20")
                ),
                responseFields(
                        fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                .description("시설 목록"),
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                .description("시설 아이디"),
                        fieldWithPath("[].districtName").type(JsonFieldType.STRING)
                                .description("시/군/구"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING)
                                .description("시설명"),
                        fieldWithPath("[].director").type(JsonFieldType.STRING)
                                .description("관리자명"),
                        fieldWithPath("[].capacity").type(JsonFieldType.NUMBER)
                                .description("정원"),
                        fieldWithPath("[].currentTotal").type(JsonFieldType.NUMBER)
                                .description("현원 - 계"),
                        fieldWithPath("[].currentMale").type(JsonFieldType.NUMBER)
                                .description("현원 - 남"),
                        fieldWithPath("[].currentFemale").type(JsonFieldType.NUMBER)
                                .description("현원 - 여"),
                        fieldWithPath("[].staffTotal").type(JsonFieldType.NUMBER)
                                .description("종사자수 - 계"),
                        fieldWithPath("[].staffMale").type(JsonFieldType.NUMBER)
                                .description("종사자수 - 남"),
                        fieldWithPath("[].staffFemale").type(JsonFieldType.NUMBER)
                                .description("종사자수 - 여"),
                        fieldWithPath("[].address").type(JsonFieldType.STRING)
                                .description("소재지"),
                        fieldWithPath("[].phoneNumber").type(JsonFieldType.STRING)
                                .description("전화번호"),
                        fieldWithPath("[].facilityType").type(JsonFieldType.STRING)
                                .description("시설 종류"),
                        fieldWithPath("[].grade").type(JsonFieldType.STRING)
                                .description("시설 등급"),
                        fieldWithPath("[].price").type(JsonFieldType.NUMBER)
                                .description("시설 종류"),
                        fieldWithPath("[].establishmentYear").type(JsonFieldType.NUMBER)
                                .description("시설연도")

                )
        ))

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
            MockMvcResultMatchers.jsonPath('$[0].grade').value(expectedResponse.get(0).grade())
            MockMvcResultMatchers.jsonPath('$[0].price').value(expectedResponse.get(0).price())
            MockMvcResultMatchers.jsonPath('$[0].establishmentYear').value(expectedResponse.get(0).establishmentYear())
        }


    }
}