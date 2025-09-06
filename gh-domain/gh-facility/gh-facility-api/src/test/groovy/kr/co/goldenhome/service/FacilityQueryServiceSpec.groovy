package kr.co.goldenhome.service


import kr.co.goldenhome.ReviewMetaData
import kr.co.goldenhome.dto.FacilityDetailServiceResponse
import kr.co.goldenhome.entity.Facility
import kr.co.goldenhome.entity.FacilityDocument
import kr.co.goldenhome.implement.FacilityReader
import kr.co.goldenhome.implement.FacilitySearcher
import kr.co.goldenhome.FacilityEventManger
import spock.lang.Specification

class FacilityQueryServiceSpec extends Specification {

    FacilityQueryService facilityService
    FacilitySearcher facilitySearcher = Mock()
    FacilityReader facilityReader = Mock()
    FacilityEventManger facilityEventManager = Mock()


    def setup() {
        facilityService = new FacilityQueryService(facilitySearcher, facilityReader, facilityEventManager)
    }

    def "search - facilitySearcher 를 호출하고 FacilityDocument 수 만큼 facilityProfileApi 를 호출한다"() {
        given:
        def givenName = "대전요양원"
        def givenAddress = "대전광역시"
        def givenFacilityType = "주야간보호"
        def givenGrade = "A"
        def givenSort = "recommend"
        def givenWithinYears = 20
        def givenPage = 1
        def givenSize = 20
        def givenResponse1 = FacilityDocument.builder().id("1").build()
        def givenResponse2 = FacilityDocument.builder().id("2").build()

        when:
        facilityService.search(givenName, givenAddress, givenFacilityType, givenGrade, givenSort, givenWithinYears, givenPage, givenSize)

        then:
        1 * facilitySearcher.search(*_) >> {
            String name, String address, String facilityType, String grade, String sort, int withinYears, int page, int size ->
                name == givenName
                address == givenAddress
                facilityType == givenFacilityType
                grade == givenGrade
                sort == givenSort
                withinYears == givenWithinYears
                page == givenPage
                size == givenSize
                List.of(givenResponse1, givenResponse2)
        }

    }

    def "read - facilityReader, facilityEventManger 를 호출한다"() {
        given:
        def givenFacilityId = 1L
        def givenUserId = 1L
        when:
        facilityService.read(givenFacilityId, givenUserId)
        then:
        1 * facilityReader.read(*_) >> {
            Long facilityId ->
                facilityId == givenFacilityId
                Mock(FacilityDetailServiceResponse)
        }
        1 * facilityReader.getReviewMetaData(*_) >> {
            Long facilityId ->
                facilityId == givenFacilityId
                Mock(ReviewMetaData)
        }
        1 * facilityReader.isLiked(*_) >> {
            Long facilityId, Long userId ->
                facilityId == givenFacilityId
                userId == givenUserId
                true
        }
        1 * facilityEventManager.saveLog(_)

    }

    def "getLikedFacilities - likeApi, facilityReader 를 호출하고 Facility 수 만큼 facilityProfileApi 를 호출한다"() {
        given:
        def givenUserId = 1L
        def givenLikeApiResponse = List.of(1L, 2L)
        def givenReaderResponse = List.of(Facility.builder().id(1L).build(), Facility.builder().id(2L).build())

        when:
        facilityService.getLikedFacilities(givenUserId)

        then:
        1 * facilityReader.getLikedFacilityIds(givenUserId) >> givenLikeApiResponse
        1 * facilityReader.getByIds(givenLikeApiResponse) >> givenReaderResponse
        2 * facilityReader.getProfileUrl(_) >> ""
        2 * facilityReader.getGradeByInstitutionSymbol(_) >> ""
    }
}
