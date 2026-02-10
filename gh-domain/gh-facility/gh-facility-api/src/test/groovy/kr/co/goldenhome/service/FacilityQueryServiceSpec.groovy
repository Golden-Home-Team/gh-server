package kr.co.goldenhome.service

import kr.co.goldenhome.ReviewMetaData
import kr.co.goldenhome.auth.UserPrincipal
import kr.co.goldenhome.dto.FacilityDetailServiceResponse
import kr.co.goldenhome.entity.Facility
import kr.co.goldenhome.entity.FacilityDocument
import kr.co.goldenhome.entity.RecentView
import kr.co.goldenhome.implement.FacilityMetaDataManager
import kr.co.goldenhome.implement.FacilityReader
import kr.co.goldenhome.implement.FacilitySearcher
import kr.co.goldenhome.FacilityEventManger
import kr.co.goldenhome.repository.RecentViewRepository
import org.springframework.data.elasticsearch.core.geo.GeoPoint
import spock.lang.Specification

class FacilityQueryServiceSpec extends Specification {

    FacilityQueryService facilityService
    FacilitySearcher facilitySearcher = Mock()
    FacilityReader facilityReader = Mock()
    FacilityEventManger facilityEventManager = Mock()
    RecentViewRepository recentViewRepository = Mock()
    FacilityMetaDataManager facilityMetaDataManager = Mock()

    def setup() {
        facilityService = new FacilityQueryService(facilitySearcher, facilityReader, facilityMetaDataManager, facilityEventManager, recentViewRepository)
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
        def givenLat = (Double) 35.1
        def givenLon = (double) 124.5
        def givenResponse1 = FacilityDocument.builder().id("1").location(new GeoPoint(givenLat, givenLon)).build()
        def givenResponse2 = FacilityDocument.builder().id("2").location(new GeoPoint(givenLat, givenLon)).build()
        def givenLatitude = 34.1
        def givenLongitude = 127.1
        def givenRadiusKm = 1
        def givenUserPrincipal = new UserPrincipal(1L)

        when:
        facilityService.search(givenName, givenAddress, givenFacilityType, givenGrade, givenSort, givenWithinYears, givenPage, givenSize, givenLatitude, givenLongitude, givenRadiusKm, givenUserPrincipal)

        then:
        1 * facilitySearcher.search(*_) >> {
            String name, String address, String facilityType, String grade, String sort, int withinYears, int page, int size, Double lat, Double lon, Double radiusKm ->
                name == givenName
                address == givenAddress
                facilityType == givenFacilityType
                grade == givenGrade
                sort == givenSort
                withinYears == givenWithinYears
                page == givenPage
                size == givenSize
                lat == givenLatitude
                lon == givenLongitude
                radiusKm == givenRadiusKm
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
        1 * facilityMetaDataManager.getReviewMetaData(*_) >> {
            Long facilityId ->
                facilityId == givenFacilityId
                Mock(ReviewMetaData)
        }
        1 * facilityMetaDataManager.isLiked(*_) >> {
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
        1 * facilityMetaDataManager.getLikedFacilityIds(givenUserId) >> givenLikeApiResponse
        1 * facilityReader.getByIds(givenLikeApiResponse) >> givenReaderResponse
        2 * facilityMetaDataManager.getProfileUrl(_) >> ""
        2 * facilityReader.getGradeByInstitutionSymbol(_) >> ""
    }

    def "recent - recentViewRepository 를 한번, facilityReader 를 여러번 호출한다"() {
        given:

        when:
        facilityService.recent(1)

        then:
        1 * recentViewRepository.findByUserId(*_) >> List.of(RecentView.builder().id(1).build())
        1 * facilityReader.getByIds(*_) >> List.of(Facility.builder().id(1).institutionSymbol("13").build())
        1 * facilityMetaDataManager.getProfileUrl(*_) >> "prf"
        1 * facilityReader.getGradeByInstitutionSymbol(*_) >> "grd"
    }
}
