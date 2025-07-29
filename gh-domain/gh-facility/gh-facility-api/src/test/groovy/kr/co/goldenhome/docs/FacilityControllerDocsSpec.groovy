package kr.co.goldenhome.docs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.FacilityDetailResponse
import kr.co.goldenhome.dto.FacilityPhotoResponse
import kr.co.goldenhome.dto.FacilityProgramResponse
import kr.co.goldenhome.dto.FacilityResponse
import kr.co.goldenhome.service.FacilityService
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class FacilityControllerDocsSpec extends Specification {

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

        def expectedResponse = List.of(new FacilityResponse(1L, "23017000292", "주야간보호 내 치매전담 1실", "대전요양원 주간보호센터", "대전광역시 서구 조달청길  116 (도마동)", 2014, "A", 25, 23))

        facilityService.search(givenName, givenAddress, givenFacilityType, givenGrade, givenSort, givenWithinYears, givenPage, givenSize)
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
        ).andDo(document("facility-search-latest",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("name").description("시설명"),
                        parameterWithName("address").description("시설 주소"),
                        parameterWithName("facilityType").description("시설종류 e.g. 주야간보호 내 치매전담 1실"),
                        parameterWithName("grade").description("시설등급 e.g. A,B..."),
                        parameterWithName("sort").description("정렬기준 (view, review, like, consultation) 아무것도 안보내면 유사도순입니다."),
                        parameterWithName("withinYears").description("설립연도 n년 이내 e.g. 1"),
                        parameterWithName("page").description("페이지 default = 1"),
                        parameterWithName("size").description("페이지 크기 default = 20")
                ),
                responseFields(
                        fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                .description("시설 목록"),
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                .description("시설 아이디"),
                        fieldWithPath("[].institutionSymbol").type(JsonFieldType.STRING)
                                .description("기관 코드"),
                        fieldWithPath("[].facilityType").type(JsonFieldType.STRING)
                                .description("시설 종류"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING)
                                .description("시설명"),
                        fieldWithPath("[].address").type(JsonFieldType.STRING)
                                .description("소재지"),
                        fieldWithPath("[].establishmentYear").type(JsonFieldType.NUMBER)
                                .description("시설연도"),
                        fieldWithPath("[].grade").type(JsonFieldType.STRING)
                                .description("시설 등급"),

                        fieldWithPath("[].capacity").type(JsonFieldType.NUMBER)
                                .description("정원"),
                        fieldWithPath("[].currentTotal").type(JsonFieldType.NUMBER)
                                .description("현원 - 계")
                )
        ))


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
                .andDo(document("facility-read-latest",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("시설 아이디"),
                                fieldWithPath("institutionSymbol").type(JsonFieldType.STRING)
                                        .description("기관 코드"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("시설명"),
                                fieldWithPath("facilityType").type(JsonFieldType.STRING)
                                        .description("시설 유형"),
                                fieldWithPath("address").type(JsonFieldType.STRING)
                                        .description("주소"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                                        .description("전화번호"),
                                fieldWithPath("establishmentDate").type(JsonFieldType.NUMBER)
                                        .description("설립연도"),
                                fieldWithPath("grade").type(JsonFieldType.STRING)
                                        .description("시설 등급"),
                                fieldWithPath("capacity").type(JsonFieldType.NUMBER)
                                        .description("정원"),
                                fieldWithPath("currentTotal").type(JsonFieldType.NUMBER)
                                        .description("현재 인원 (총계)"),
                                fieldWithPath("currentMale").type(JsonFieldType.NUMBER)
                                        .description("현재 인원 (남성)"),
                                fieldWithPath("currentFemale").type(JsonFieldType.NUMBER)
                                        .description("현재 인원 (여성)"),

                                // FacilityInfoResponse
                                fieldWithPath("facilityInfoResponse.facilityDetailId").type(JsonFieldType.NUMBER)
                                        .description("시설 상세 정보 아이디"),
                                fieldWithPath("facilityInfoResponse.singleRoomCount").type(JsonFieldType.STRING)
                                        .description("1인실 수"),
                                fieldWithPath("facilityInfoResponse.doubleRoomCount").type(JsonFieldType.STRING)
                                        .description("2인실 수"),
                                fieldWithPath("facilityInfoResponse.tripleRoomCount").type(JsonFieldType.STRING)
                                        .description("3인실 수"),
                                fieldWithPath("facilityInfoResponse.quadRoomCount").type(JsonFieldType.STRING)
                                        .description("4인실 수"),
                                fieldWithPath("facilityInfoResponse.officeCount").type(JsonFieldType.STRING)
                                        .description("사무실 수"),
                                fieldWithPath("facilityInfoResponse.medicalNurseRoomCount").type(JsonFieldType.STRING)
                                        .description("의료 및 간호사실 수"),
                                fieldWithPath("facilityInfoResponse.dailyLivingTrainingRoomCount").type(JsonFieldType.STRING)
                                        .description("일상생활 훈련실 수"),
                                fieldWithPath("facilityInfoResponse.programRoomCount").type(JsonFieldType.STRING)
                                        .description("프로그램실 수"),
                                fieldWithPath("facilityInfoResponse.kitchenDiningRoomCount").type(JsonFieldType.STRING)
                                        .description("주방 및 식당 수"), // 또는 해당 필드에 대한 정확한 설명
                                fieldWithPath("facilityInfoResponse.bathroomCount").type(JsonFieldType.STRING)
                                        .description("화장실 수"),
                                fieldWithPath("facilityInfoResponse.washBathRoomCount").type(JsonFieldType.STRING)
                                        .description("세면 및 목욕실 수"),
                                fieldWithPath("facilityInfoResponse.laundryRoomCount").type(JsonFieldType.STRING)
                                        .description("세탁물 보관실 수"),

                                // FacilityStaffResponse
                                fieldWithPath("facilityStaffResponse.facilityStaffInformationId").type(JsonFieldType.NUMBER)
                                        .description("시설 직원 정보 아이디"),
                                fieldWithPath("facilityStaffResponse.directorCount").type(JsonFieldType.NUMBER)
                                        .description("시설장 수"),
                                fieldWithPath("facilityStaffResponse.headOfOfficeCount").type(JsonFieldType.NUMBER)
                                        .description("사무국장 수"),
                                fieldWithPath("facilityStaffResponse.socialWorkerCount").type(JsonFieldType.NUMBER)
                                        .description("사회복지사 수"),
                                fieldWithPath("facilityStaffResponse.residentDoctorCount").type(JsonFieldType.NUMBER)
                                        .description("상근 의사 수"),
                                fieldWithPath("facilityStaffResponse.visitingDoctorCount").type(JsonFieldType.NUMBER)
                                        .description("방문 의사 수"),
                                fieldWithPath("facilityStaffResponse.nurseCount").type(JsonFieldType.NUMBER)
                                        .description("간호사 수"),
                                fieldWithPath("facilityStaffResponse.assistantNurseCount").type(JsonFieldType.NUMBER)
                                        .description("간호조무사 수"),
                                fieldWithPath("facilityStaffResponse.dentalHygienistCount").type(JsonFieldType.NUMBER)
                                        .description("치위생사 수"),
                                fieldWithPath("facilityStaffResponse.physicalTherapistCount").type(JsonFieldType.NUMBER)
                                        .description("물리치료사 수"),
                                fieldWithPath("facilityStaffResponse.occupationalTherapistCount").type(JsonFieldType.NUMBER)
                                        .description("작업치료사 수"),
                                fieldWithPath("facilityStaffResponse.caregiverLevel1Count").type(JsonFieldType.NUMBER)
                                        .description("요양보호사 1등급 수"),
                                fieldWithPath("facilityStaffResponse.caregiverLevel2Count").type(JsonFieldType.NUMBER)
                                        .description("요양보호사 2등급 수"),
                                fieldWithPath("facilityStaffResponse.caregiverDeferredCount").type(JsonFieldType.NUMBER)
                                        .description("요양보호사 미정 등급 수"),
                                fieldWithPath("facilityStaffResponse.officeWorkerCount").type(JsonFieldType.NUMBER)
                                        .description("사무원 수"),
                                fieldWithPath("facilityStaffResponse.dietitianCount").type(JsonFieldType.NUMBER)
                                        .description("영양사 수"),
                                fieldWithPath("facilityStaffResponse.cookCount").type(JsonFieldType.NUMBER)
                                        .description("조리원 수"),
                                fieldWithPath("facilityStaffResponse.hygieneWorkerCount").type(JsonFieldType.NUMBER)
                                        .description("위생원 수"),
                                fieldWithPath("facilityStaffResponse.maintenanceWorkerCount").type(JsonFieldType.NUMBER)
                                        .description("관리인 수"),
                                fieldWithPath("facilityStaffResponse.assistantWorkerCount").type(JsonFieldType.NUMBER)
                                        .description("보조원 수"),
                                fieldWithPath("facilityStaffResponse.otherWorkerCount").type(JsonFieldType.NUMBER)
                                        .description("기타 직원 수"),

                                // FacilityPhotoResponse List
                                fieldWithPath("photoResponses.[].id").type(JsonFieldType.NUMBER)
                                        .description("시설 사진 아이디"),
                                fieldWithPath("photoResponses.[].institutionSymbol").type(JsonFieldType.STRING)
                                        .description("시설 사진 기관 코드"),
                                fieldWithPath("photoResponses.[].type").type(JsonFieldType.STRING)
                                        .description("시설 사진 유형"),
                                fieldWithPath("photoResponses.[].name").type(JsonFieldType.STRING)
                                        .description("시설 사진명"),
                                fieldWithPath("photoResponses.[].imageUrl").type(JsonFieldType.STRING)
                                        .description("시설 사진 URL"),
                                fieldWithPath("photoResponses.[].description").type(JsonFieldType.STRING)
                                        .description("시설 사진 설명"),

                                // FacilityProgramResponse List
                                fieldWithPath("facilityProgramResponses.[].id").type(JsonFieldType.NUMBER)
                                        .description("시설 프로그램 아이디"),
                                fieldWithPath("facilityProgramResponses.[].institutionSymbol").type(JsonFieldType.STRING)
                                        .description("시설 프로그램 기관 코드"),
                                fieldWithPath("facilityProgramResponses.[].type").type(JsonFieldType.STRING)
                                        .description("시설 프로그램 유형"),
                                fieldWithPath("facilityProgramResponses.[].name").type(JsonFieldType.STRING)
                                        .description("시설 프로그램명")
                        )
                )
                )

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
