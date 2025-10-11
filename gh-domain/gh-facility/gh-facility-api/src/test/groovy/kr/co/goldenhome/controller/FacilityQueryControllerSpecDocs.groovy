package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.auth.UserPrincipal
import kr.co.goldenhome.dto.FacilityDetailResponse
import kr.co.goldenhome.dto.FacilityInfoInnerResponse
import kr.co.goldenhome.dto.FacilityPhotoResponse
import kr.co.goldenhome.dto.FacilityProgramResponse
import kr.co.goldenhome.dto.FacilityResponse
import kr.co.goldenhome.dto.FacilityStaffInnerResponse
import kr.co.goldenhome.service.FacilityQueryService
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class FacilityQueryControllerSpecDocs extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    FacilityQueryService facilityService = Mock()

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
        def givenLatitude = 34.1
        def givenLongitude = 127.1
        def givenRadiusKm = 1
        def givenUserPrincipal = new UserPrincipal(1L)

        def expectedResponse = List.of(new FacilityResponse(1L, "23017000292", "주야간보호 내 치매전담 1실", "대전요양원 주간보호센터", "대전광역시 서구 조달청길  116 (도마동)", 2014, "A", 25, 23, "https://", 37.1, 126.4, false))

        facilityService.search(givenName, givenAddress, givenFacilityType, givenGrade, givenSort, givenWithinYears, givenPage, givenSize, givenLatitude, givenLongitude, givenRadiusKm, givenUserPrincipal)
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
                        .param("lat", givenLatitude as String)
                        .param("lon", givenLongitude as String)
                        .param("radiusKm", givenRadiusKm as String)
                .principal(givenUserPrincipal)
        ).andDo(document("facility-search",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("name").description("시설명"),
                        parameterWithName("address").description("시설 주소"),
                        parameterWithName("facilityType").description(
                                "실버타운, 양로원 -> 시설명으로 검색" +
                                "요양원 -> 노인요양시설 + 노인요양공동생활가정 시설종류로 검색 " +
                                "그 외 -> 시설종류 그대로 검색"),
                        parameterWithName("grade").description("시설등급 e.g. A,B..."),
                        parameterWithName("sort").description("정렬기준 (view, review, like, consultation) 아무것도 안보내면 유사도순입니다."),
                        parameterWithName("withinYears").description("설립연도 n년 이내 e.g. 1"),
                        parameterWithName("page").description("페이지 default = 1"),
                        parameterWithName("size").description("페이지 크기 default = 20"),
                        parameterWithName("lat").description("클라이언트 위도"),
                        parameterWithName("lon").description("클라이언트 경도"),
                        parameterWithName("radiusKm").description("반경 n km 이내, e.g. 0.1, 1, 5..."),
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
                                .description("현원 - 계"),
                        fieldWithPath("[].profileUrl").type(JsonFieldType.STRING)
                                .description("시설 프로필 이미지"),
                        fieldWithPath("[].latitude").type(JsonFieldType.NUMBER)
                                .description("위도"),
                        fieldWithPath("[].longitude").type(JsonFieldType.NUMBER)
                                .description("경도"),
                        fieldWithPath("[].isLiked").type(JsonFieldType.BOOLEAN)
                                .description("좋아요 여부")

                )
        ))


        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
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
                36.1,
                125.3,
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
                true,
                1L
        )


        facilityService.read(*_) >> expectedResponse
        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/facilities/{facilityId}", givenFacilityId))

                .andDo(document("facility-read",
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
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER)
                                        .description("경도"),

                                // facilityInfoInnerResponse
                                fieldWithPath("facilityInfoInnerResponse.facilityDetailId").type(JsonFieldType.NUMBER)
                                        .description("시설 상세 정보 아이디"),
                                fieldWithPath("facilityInfoInnerResponse.singleRoomCount").type(JsonFieldType.STRING)
                                        .description("1인실 수"),
                                fieldWithPath("facilityInfoInnerResponse.doubleRoomCount").type(JsonFieldType.STRING)
                                        .description("2인실 수"),
                                fieldWithPath("facilityInfoInnerResponse.tripleRoomCount").type(JsonFieldType.STRING)
                                        .description("3인실 수"),
                                fieldWithPath("facilityInfoInnerResponse.quadRoomCount").type(JsonFieldType.STRING)
                                        .description("4인실 수"),
                                fieldWithPath("facilityInfoInnerResponse.officeCount").type(JsonFieldType.STRING)
                                        .description("사무실 수"),
                                fieldWithPath("facilityInfoInnerResponse.medicalNurseRoomCount").type(JsonFieldType.STRING)
                                        .description("의료 및 간호사실 수"),
                                fieldWithPath("facilityInfoInnerResponse.dailyLivingTrainingRoomCount").type(JsonFieldType.STRING)
                                        .description("일상생활 훈련실 수"),
                                fieldWithPath("facilityInfoInnerResponse.programRoomCount").type(JsonFieldType.STRING)
                                        .description("프로그램실 수"),
                                fieldWithPath("facilityInfoInnerResponse.kitchenDiningRoomCount").type(JsonFieldType.STRING)
                                        .description("주방 및 식당 수"), // 또는 해당 필드에 대한 정확한 설명
                                fieldWithPath("facilityInfoInnerResponse.bathroomCount").type(JsonFieldType.STRING)
                                        .description("화장실 수"),
                                fieldWithPath("facilityInfoInnerResponse.washBathRoomCount").type(JsonFieldType.STRING)
                                        .description("세면 및 목욕실 수"),
                                fieldWithPath("facilityInfoInnerResponse.laundryRoomCount").type(JsonFieldType.STRING)
                                        .description("세탁물 보관실 수"),

                                // facilityStaffInnerResponse
                                fieldWithPath("facilityStaffInnerResponse.facilityStaffInformationId").type(JsonFieldType.NUMBER)
                                        .description("시설 직원 정보 아이디"),
                                fieldWithPath("facilityStaffInnerResponse.directorCount").type(JsonFieldType.NUMBER)
                                        .description("시설장 수"),
                                fieldWithPath("facilityStaffInnerResponse.headOfOfficeCount").type(JsonFieldType.NUMBER)
                                        .description("사무국장 수"),
                                fieldWithPath("facilityStaffInnerResponse.socialWorkerCount").type(JsonFieldType.NUMBER)
                                        .description("사회복지사 수"),
                                fieldWithPath("facilityStaffInnerResponse.residentDoctorCount").type(JsonFieldType.NUMBER)
                                        .description("상근 의사 수"),
                                fieldWithPath("facilityStaffInnerResponse.visitingDoctorCount").type(JsonFieldType.NUMBER)
                                        .description("방문 의사 수"),
                                fieldWithPath("facilityStaffInnerResponse.nurseCount").type(JsonFieldType.NUMBER)
                                        .description("간호사 수"),
                                fieldWithPath("facilityStaffInnerResponse.assistantNurseCount").type(JsonFieldType.NUMBER)
                                        .description("간호조무사 수"),
                                fieldWithPath("facilityStaffInnerResponse.dentalHygienistCount").type(JsonFieldType.NUMBER)
                                        .description("치위생사 수"),
                                fieldWithPath("facilityStaffInnerResponse.physicalTherapistCount").type(JsonFieldType.NUMBER)
                                        .description("물리치료사 수"),
                                fieldWithPath("facilityStaffInnerResponse.occupationalTherapistCount").type(JsonFieldType.NUMBER)
                                        .description("작업치료사 수"),
                                fieldWithPath("facilityStaffInnerResponse.caregiverLevel1Count").type(JsonFieldType.NUMBER)
                                        .description("요양보호사 1등급 수"),
                                fieldWithPath("facilityStaffInnerResponse.caregiverLevel2Count").type(JsonFieldType.NUMBER)
                                        .description("요양보호사 2등급 수"),
                                fieldWithPath("facilityStaffInnerResponse.caregiverDeferredCount").type(JsonFieldType.NUMBER)
                                        .description("요양보호사 미정 등급 수"),
                                fieldWithPath("facilityStaffInnerResponse.officeWorkerCount").type(JsonFieldType.NUMBER)
                                        .description("사무원 수"),
                                fieldWithPath("facilityStaffInnerResponse.dietitianCount").type(JsonFieldType.NUMBER)
                                        .description("영양사 수"),
                                fieldWithPath("facilityStaffInnerResponse.cookCount").type(JsonFieldType.NUMBER)
                                        .description("조리원 수"),
                                fieldWithPath("facilityStaffInnerResponse.hygieneWorkerCount").type(JsonFieldType.NUMBER)
                                        .description("위생원 수"),
                                fieldWithPath("facilityStaffInnerResponse.maintenanceWorkerCount").type(JsonFieldType.NUMBER)
                                        .description("관리인 수"),
                                fieldWithPath("facilityStaffInnerResponse.assistantWorkerCount").type(JsonFieldType.NUMBER)
                                        .description("보조원 수"),
                                fieldWithPath("facilityStaffInnerResponse.otherWorkerCount").type(JsonFieldType.NUMBER)
                                        .description("기타 직원 수"),

                                // FacilityPhotoResponse List
                                fieldWithPath("photoResponses[].id").type(JsonFieldType.NUMBER)
                                        .description("시설 사진 아이디"),
                                fieldWithPath("photoResponses[].institutionSymbol").type(JsonFieldType.STRING)
                                        .description("시설 사진 기관 코드"),
                                fieldWithPath("photoResponses[].type").type(JsonFieldType.STRING)
                                        .description("시설 사진 유형"),
                                fieldWithPath("photoResponses[].name").type(JsonFieldType.STRING)
                                        .description("시설 사진명"),
                                fieldWithPath("photoResponses[].imageUrl").type(JsonFieldType.STRING)
                                        .description("시설 사진 URL"),
                                fieldWithPath("photoResponses[].description").type(JsonFieldType.STRING)
                                        .description("시설 사진 설명"),

                                // FacilityProgramResponse List
                                fieldWithPath("facilityProgramResponses.[].id").type(JsonFieldType.NUMBER)
                                        .description("시설 프로그램 아이디"),
                                fieldWithPath("facilityProgramResponses.[].institutionSymbol").type(JsonFieldType.STRING)
                                        .description("시설 프로그램 기관 코드"),
                                fieldWithPath("facilityProgramResponses.[].type").type(JsonFieldType.STRING)
                                        .description("시설 프로그램 유형"),
                                fieldWithPath("facilityProgramResponses.[].name").type(JsonFieldType.STRING)
                                        .description("시설 프로그램명"),

                                // Meta Data
                                fieldWithPath("averageScore").type(JsonFieldType.NUMBER)
                                        .description("리뷰 평점"),
                                fieldWithPath("totalCount").type(JsonFieldType.NUMBER)
                                        .description("리뷰 수"),
                                fieldWithPath("onePointCount").type(JsonFieldType.NUMBER)
                                        .description("리뷰 - 1점 개수"),
                                fieldWithPath("twoPointCount").type(JsonFieldType.NUMBER)
                                        .description("리뷰 - 2점 개수"),
                                fieldWithPath("threePointCount").type(JsonFieldType.NUMBER)
                                        .description("리뷰 - 3점 개수"),
                                fieldWithPath("fourPointCount").type(JsonFieldType.NUMBER)
                                        .description("리뷰 - 4점 개수"),
                                fieldWithPath("fivePointCount").type(JsonFieldType.NUMBER)
                                        .description("리뷰 - 5점 개수"),
                                fieldWithPath("isLiked").type(JsonFieldType.BOOLEAN)
                                        .description("본인 좋아요 여부"),
                                fieldWithPath("viewCount").type(JsonFieldType.NUMBER)
                                        .description("조회수")
                        )
                )
                )

        then:

        response.andExpect {
            MockMvcResultMatchers.status().isOk()

        }
    }

    def "좋아요한 시설 목록 조회"() {
        given:

        def expectedResponse = List.of(new FacilityResponse(1L, "23017000292", "주야간보호 내 치매전담 1실", "대전요양원 주간보호센터", "대전광역시 서구 조달청길  116 (도마동)", 2014, "A", 25, 23, "https://", 36.4, 127.1, true))

        facilityService.getLikedFacilities(*_)
                >> expectedResponse

        when:
        def response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/facilities/like")
                        .principal(new UserPrincipal(1L))

        ).andDo(document("facility-read-liked",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
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
                                .description("현원 - 계"),
                        fieldWithPath("[].profileUrl").type(JsonFieldType.STRING)
                                .description("시설 프로필 이미지"),
                        fieldWithPath("[].latitude").type(JsonFieldType.NUMBER)
                                .description("시설 프로필 이미지"),
                        fieldWithPath("[].longitude").type(JsonFieldType.NUMBER)
                                .description("시설 프로필 이미지"),
                        fieldWithPath("[].isLiked").type(JsonFieldType.BOOLEAN)
                                .description("시설 좋아요 여부")

                )
        ))


        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }

    }
}
