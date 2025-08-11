package kr.co.goldenhome.service

import kr.co.goldenhome.LikeApi
import kr.co.goldenhome.ReviewApi
import kr.co.goldenhome.ReviewMetaData
import kr.co.goldenhome.dto.FacilityDetailServiceResponse
import kr.co.goldenhome.entity.FacilityDocument
import kr.co.goldenhome.implement.FacilityReader
import kr.co.goldenhome.implement.FacilitySearcher
import spock.lang.Specification

class FacilityServiceSpec extends Specification {

    FacilityService facilityService
    FacilitySearcher facilitySearcher = Mock()
    FacilityReader facilityReader = Mock()
    ReviewApi reviewApi = Mock()
    LikeApi likeApi = Mock()


    def setup() {
        facilityService = new FacilityService(facilitySearcher, facilityReader, reviewApi, likeApi)
    }

    def "search - facilitySearcher 를 호출한다"() {
        given:
        def givenName = "대전요양원"
        def givenAddress = "대전광역시"
        def givenFacilityType = "주야간보호"
        def givenGrade = "A"
        def givenSort = "recommend"
        def givenWithinYears = 20
        def givenPage = 1
        def givenSize = 20

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
                List.of(FacilityDocument.builder().id("1").build())
        }
    }

    def "read - facilityReader, reviewApi, likeApi 를 호출한다"() {
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
        1 * reviewApi.getReviewMetaData(*_) >> {
            Long facilityId ->
                facilityId == givenFacilityId
                Mock(ReviewMetaData)
        }
        1 * likeApi.isLiked(*_) >> {
            Long facilityId, Long userId ->
                facilityId == givenFacilityId
                userId == givenUserId
                true
        }

    }
}
