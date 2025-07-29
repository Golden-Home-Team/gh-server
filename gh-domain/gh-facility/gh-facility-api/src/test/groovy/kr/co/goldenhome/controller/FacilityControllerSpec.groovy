package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.FacilityDetailResponse
import kr.co.goldenhome.dto.FacilityPhotoResponse
import kr.co.goldenhome.dto.FacilityProgramResponse
import kr.co.goldenhome.dto.FacilityResponse
import kr.co.goldenhome.entity.FacilityStaffInformation
import kr.co.goldenhome.service.FacilityService
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
        )

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
                new FacilityDetailResponse.FacilityInfoResponse(
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
                new FacilityDetailResponse.FacilityStaffResponse(
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
                ))
        )


        facilityService.read(givenFacilityId) >> expectedResponse
        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/facilities/{facilityId}", givenFacilityId))

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
            MockMvcResultMatchers.jsonPath('$.facilityInfoResponse.facilityDetailId').value(expectedResponse.facilityInfoResponse().facilityDetailId())
            MockMvcResultMatchers.jsonPath('$.facilityInfoResponse.singleRoomCount').value(expectedResponse.facilityInfoResponse().singleRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoResponse.doubleRoomCount').value(expectedResponse.facilityInfoResponse().doubleRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoResponse.tripleRoomCount').value(expectedResponse.facilityInfoResponse().tripleRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoResponse.quadRoomCount').value(expectedResponse.facilityInfoResponse().quadRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoResponse.officeCount').value(expectedResponse.facilityInfoResponse().officeCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoResponse.medicalNurseRoomCount').value(expectedResponse.facilityInfoResponse().medicalNurseRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoResponse.dailyLivingTrainingRoomCount').value(expectedResponse.facilityInfoResponse().dailyLivingTrainingRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoResponse.programRoomCount').value(expectedResponse.facilityInfoResponse().programRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoResponse.bathroomCount').value(expectedResponse.facilityInfoResponse().bathroomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoResponse.washBathRoomCount').value(expectedResponse.facilityInfoResponse().washBathRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityInfoResponse.laundryRoomCount').value(expectedResponse.facilityInfoResponse().laundryRoomCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.facilityStaffInformationId').value(expectedResponse.facilityStaffResponse().facilityStaffInformationId())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.directorCount').value(expectedResponse.facilityStaffResponse().directorCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.headOfOfficeCount').value(expectedResponse.facilityStaffResponse().headOfOfficeCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.socialWorkerCount').value(expectedResponse.facilityStaffResponse().socialWorkerCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.residentDoctorCount').value(expectedResponse.facilityStaffResponse().residentDoctorCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.visitingDoctorCount').value(expectedResponse.facilityStaffResponse().visitingDoctorCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.nurseCount').value(expectedResponse.facilityStaffResponse().nurseCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.assistantNurseCount').value(expectedResponse.facilityStaffResponse().assistantNurseCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.dentalHygienistCount').value(expectedResponse.facilityStaffResponse().dentalHygienistCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.physicalTherapistCount').value(expectedResponse.facilityStaffResponse().physicalTherapistCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.occupationalTherapistCount').value(expectedResponse.facilityStaffResponse().occupationalTherapistCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.caregiverLevel1Count').value(expectedResponse.facilityStaffResponse().caregiverLevel1Count())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.caregiverLevel2Count').value(expectedResponse.facilityStaffResponse().caregiverLevel2Count())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.caregiverDeferredCount').value(expectedResponse.facilityStaffResponse().caregiverDeferredCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.officeWorkerCount').value(expectedResponse.facilityStaffResponse().officeWorkerCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.dietitianCount').value(expectedResponse.facilityStaffResponse().dietitianCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.cookCount').value(expectedResponse.facilityStaffResponse().cookCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.hygieneWorkerCount').value(expectedResponse.facilityStaffResponse().hygieneWorkerCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.maintenanceWorkerCount').value(expectedResponse.facilityStaffResponse().maintenanceWorkerCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.assistantWorkerCount').value(expectedResponse.facilityStaffResponse().assistantWorkerCount())
            MockMvcResultMatchers.jsonPath('$.facilityStaffResponse.otherWorkerCount').value(expectedResponse.facilityStaffResponse().otherWorkerCount())
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

        }
    }
}
