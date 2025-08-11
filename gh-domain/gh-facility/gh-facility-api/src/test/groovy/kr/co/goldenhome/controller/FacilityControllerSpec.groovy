package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.FacilityDetailResponse
import kr.co.goldenhome.dto.FacilityDetailServiceResponse
import kr.co.goldenhome.dto.FacilityInfoInnerResponse
import kr.co.goldenhome.dto.FacilityPhotoResponse
import kr.co.goldenhome.dto.FacilityProgramResponse
import kr.co.goldenhome.dto.FacilityResponse
import kr.co.goldenhome.dto.FacilityStaffInnerResponse
import kr.co.goldenhome.service.FacilityService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class FacilityControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    FacilityService facilityService = Mock()

    def "시설 검색"() {
        given:
        def givenName = "대전요양원"
        def givenAddress = "대전광역시"
        def givenFacilityType = "주야간보호"
        def givenGrade = "A"
        def givenSort = "recommend"
        def givenWithinYears = 20
        def givenPage = 1
        def givenSize = 20
        def expectedResponse = List.of(new FacilityResponse(
                1L,
                "2494994834394",
                givenFacilityType,
                givenName,
                givenAddress,
                2023,
                givenGrade,
                30,
                27
        ))

        1 * facilityService.search(givenName, givenAddress, givenFacilityType, givenGrade, givenSort, givenWithinYears, givenPage, givenSize)
        >> expectedResponse

        when:
        def response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/facilities/search")
                        .param("name", givenName)
                        .param("address", givenAddress)
                        .param("facilityType", givenFacilityType)
                        .param("grade", givenGrade)
                        .param("sort", "recommend")
                        .param("withinYears", givenWithinYears as String)
                        .param("page", givenPage as String)
                        .param("size", givenSize as String)
        ).andDo(MockMvcResultHandlers.print())

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$[0].id').value(expectedResponse.get(0).id())
            MockMvcResultMatchers.jsonPath('$[0].institutionSymbol').value(expectedResponse.get(0).institutionSymbol())
            MockMvcResultMatchers.jsonPath('$[0].facilityType').value(expectedResponse.get(0).facilityType())
            MockMvcResultMatchers.jsonPath('$[0].name').value(expectedResponse.get(0).name())
            MockMvcResultMatchers.jsonPath('$[0].address').value(expectedResponse.get(0).address())
            MockMvcResultMatchers.jsonPath('$[0].establishmentYear').value(expectedResponse.get(0).establishmentYear())
            MockMvcResultMatchers.jsonPath('$[0].grade').value(expectedResponse.get(0).grade())
            MockMvcResultMatchers.jsonPath('$[0].capacity').value(expectedResponse.get(0).capacity())
            MockMvcResultMatchers.jsonPath('$[0].currentTotal').value(expectedResponse.get(0).currentTotal())

        }

    }

    def "시설 조회"() {
        given:
        def givenFacilityId = 1L
        def givenUserId = 1L
        def expectedResponse = new FacilityDetailResponse(
                50L,
                "24713000311",
                "청우산대주간보호센터",
                "주야간보호 내 치매전담 1실",
                "경상북도 경주시 안강읍 구부랑두림길  126-9 (안강읍)",
                "054-761-7500",
                2022,
                "B",
                25,
                22,
                8,
                14,
                new FacilityInfoInnerResponse(
                        50L,
                        "0",
                        "0",
                        "0",
                        "0",
                        "0",
                        "0",
                        "0",
                        "0",
                        "0",
                        "0",
                        "0",
                        "0"
                ),
                new FacilityStaffInnerResponse(
                        40L,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        5,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0
                ),
                List.of(new FacilityPhotoResponse(
                        189L,
                        "24713000311",
                        "시설설비상태 2",
                        "건식족욕기",
                        "https://www.longtermcare.or.kr/npbs/r/a/201/selectLtcoSrchDetail.web/npbs/e/d/101/selectPhotoStreamDocNo.web?atmtFileDocNo=ST002165623",
                        "건식족욕기"
                )),
                List.of(new FacilityProgramResponse(
                        56L,
                        "24713000311",
                        "기타",
                        "노래방"
                )),
                5.0,
                1,
                0,0,0,0,1,
                true
        )


        facilityService.read(*_) >> expectedResponse
        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/facilities/{facilityId}", givenFacilityId))
                .andDo(MockMvcResultHandlers.print())

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.id').value(expectedResponse.id())
            MockMvcResultMatchers.jsonPath('$.institutionSymbol').value(expectedResponse.institutionSymbol())
            MockMvcResultMatchers.jsonPath('$.name').value(expectedResponse.name())
            MockMvcResultMatchers.jsonPath('$.facilityType').value(expectedResponse.facilityType())
            MockMvcResultMatchers.jsonPath('$.address').value(expectedResponse.address())
            MockMvcResultMatchers.jsonPath('$.phoneNumber').value(expectedResponse.phoneNumber())
            MockMvcResultMatchers.jsonPath('$.establishmentDate').value(expectedResponse.establishmentDate())
            MockMvcResultMatchers.jsonPath('$.grade').value(expectedResponse.grade())
            MockMvcResultMatchers.jsonPath('$.capacity').value(expectedResponse.capacity())
            MockMvcResultMatchers.jsonPath('$.currentTotal').value(expectedResponse.currentTotal())
            MockMvcResultMatchers.jsonPath('$.currentMale').value(expectedResponse.currentMale())
            MockMvcResultMatchers.jsonPath('$.currentFemale').value(expectedResponse.currentFemale())
            MockMvcResultMatchers.jsonPath('$.facilityInfoInnerResponse.facilityDetailId').value(expectedResponse.facilityInfoInnerResponse().facilityDetailId())
            MockMvcResultMatchers.jsonPath('$.facilityInfoInnerResponse.singleRoomCount').value(expectedResponse.facilityInfoInnerResponse().singleRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoInnerResponse.doubleRoomCount').value(expectedResponse.facilityInfoInnerResponse().doubleRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoInnerResponse.tripleRoomCount').value(expectedResponse.facilityInfoInnerResponse().tripleRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoInnerResponse.quadRoomCount').value(expectedResponse.facilityInfoInnerResponse().quadRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoInnerResponse.officeCount').value(expectedResponse.facilityInfoInnerResponse().officeCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoInnerResponse.medicalNurseRoomCount').value(expectedResponse.facilityInfoInnerResponse().medicalNurseRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoInnerResponse.dailyLivingTrainingRoomCount').value(expectedResponse.facilityInfoInnerResponse().dailyLivingTrainingRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoInnerResponse.programRoomCount').value(expectedResponse.facilityInfoInnerResponse().programRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoInnerResponse.bathroomCount').value(expectedResponse.facilityInfoInnerResponse().bathroomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoInnerResponse.washBathRoomCount').value(expectedResponse.facilityInfoInnerResponse().washBathRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoInnerResponse.laundryRoomCount').value(expectedResponse.facilityInfoInnerResponse().laundryRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.facilityStaffInformationId').value(expectedResponse.facilityStaffInnerResponse().facilityStaffInformationId())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.directorCount').value(expectedResponse.facilityStaffInnerResponse().directorCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.headOfOfficeCount').value(expectedResponse.facilityStaffInnerResponse().headOfOfficeCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.socialWorkerCount').value(expectedResponse.facilityStaffInnerResponse().socialWorkerCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.residentDoctorCount').value(expectedResponse.facilityStaffInnerResponse().residentDoctorCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.visitingDoctorCount').value(expectedResponse.facilityStaffInnerResponse().visitingDoctorCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.nurseCount').value(expectedResponse.facilityStaffInnerResponse().nurseCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.assistantNurseCount').value(expectedResponse.facilityStaffInnerResponse().assistantNurseCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.dentalHygienistCount').value(expectedResponse.facilityStaffInnerResponse().dentalHygienistCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.physicalTherapistCount').value(expectedResponse.facilityStaffInnerResponse().physicalTherapistCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.occupationalTherapistCount').value(expectedResponse.facilityStaffInnerResponse().occupationalTherapistCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.caregiverLevel1Count').value(expectedResponse.facilityStaffInnerResponse().caregiverLevel1Count())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.caregiverLevel2Count').value(expectedResponse.facilityStaffInnerResponse().caregiverLevel2Count())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.caregiverDeferredCount').value(expectedResponse.facilityStaffInnerResponse().caregiverDeferredCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.officeWorkerCount').value(expectedResponse.facilityStaffInnerResponse().officeWorkerCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.dietitianCount').value(expectedResponse.facilityStaffInnerResponse().dietitianCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.cookCount').value(expectedResponse.facilityStaffInnerResponse().cookCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.hygieneWorkerCount').value(expectedResponse.facilityStaffInnerResponse().hygieneWorkerCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.maintenanceWorkerCount').value(expectedResponse.facilityStaffInnerResponse().maintenanceWorkerCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.assistantWorkerCount').value(expectedResponse.facilityStaffInnerResponse().assistantWorkerCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffInnerResponse.otherWorkerCount').value(expectedResponse.facilityStaffInnerResponse().otherWorkerCount())
            MockMvcResultMatchers.jsonPath('$.photoResponses.[0].id').value(expectedResponse.photoResponses().get(0).id())
            MockMvcResultMatchers.jsonPath('$.photoResponses.[0].institutionSymbol').value(expectedResponse.photoResponses().get(0).institutionSymbol())
            MockMvcResultMatchers.jsonPath('$.photoResponses.[0].type').value(expectedResponse.photoResponses().get(0).type())
            MockMvcResultMatchers.jsonPath('$.photoResponses.[0].name').value(expectedResponse.photoResponses().get(0).name())
            MockMvcResultMatchers.jsonPath('$.photoResponses.[0].imageUrl').value(expectedResponse.photoResponses().get(0).imageUrl())
            MockMvcResultMatchers.jsonPath('$.photoResponses.[0].description').value(expectedResponse.photoResponses().get(0).description())
            MockMvcResultMatchers.jsonPath('$.facilityProgramResponses.[0].id').value(expectedResponse.facilityProgramResponses().get(0).id())
            MockMvcResultMatchers.jsonPath('$.facilityProgramResponses.[0].institutionSymbol').value(expectedResponse.facilityProgramResponses().get(0).institutionSymbol())
            MockMvcResultMatchers.jsonPath('$.facilityProgramResponses.[0].type').value(expectedResponse.facilityProgramResponses().get(0).type())
            MockMvcResultMatchers.jsonPath('$.facilityProgramResponses.[0].name').value(expectedResponse.facilityProgramResponses().get(0).name())
            MockMvcResultMatchers.jsonPath('$.averageScore').value(expectedResponse.averageScore())
            MockMvcResultMatchers.jsonPath('$.totalCount').value(expectedResponse.totalCount())
            MockMvcResultMatchers.jsonPath('$.onePointCount').value(expectedResponse.onePointCount())
            MockMvcResultMatchers.jsonPath('$.twoPointCount').value(expectedResponse.twoPointCount())
            MockMvcResultMatchers.jsonPath('$.threePointCount').value(expectedResponse.threePointCount())
            MockMvcResultMatchers.jsonPath('$.fourPointCount').value(expectedResponse.fourPointCount())
            MockMvcResultMatchers.jsonPath('$.fivePointCount').value(expectedResponse.fivePointCount())
            MockMvcResultMatchers.jsonPath('$.isLiked').value(expectedResponse.isLiked())

        }
    }
}
