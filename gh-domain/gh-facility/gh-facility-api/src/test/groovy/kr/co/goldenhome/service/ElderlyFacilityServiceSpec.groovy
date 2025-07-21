package kr.co.goldenhome.service

import kr.co.goldenhome.entity.ElderlyFacility
import kr.co.goldenhome.entity.ElderlyFacilityDocument
import kr.co.goldenhome.implement.ElderlyFacilityReader
import kr.co.goldenhome.implement.ElderlyFacilitySearcher
import kr.co.goldenhome.repository.ElderlyFacilityRepository
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import spock.lang.Specification

class ElderlyFacilityServiceSpec extends Specification {

    ElderlyFacilityService elderlyFacilityService
    ElderlyFacilityReader elderlyFacilityReader = Mock()
    ElderlyFacilitySearcher elderlyFacilitySearcher = Mock()

    def setup() {
        elderlyFacilityService = new ElderlyFacilityService(elderlyFacilityReader, elderlyFacilitySearcher)
    }

    def "read - elderlyFacilityReader 를 호출한다"() {

        given:
        def givenFacilityId = 1L
        def expectedElderlyFacility = ElderlyFacility.builder().id(1L).build()

        when:
        elderlyFacilityService.read(givenFacilityId)

        then:
        1 * elderlyFacilityReader.read(*_) >> {
            Long facilityId ->
                facilityId == givenFacilityId
                expectedElderlyFacility
        }
    }

    def "readAll - elderlyFacilityReader 를 호출한다"() {
        given:
        def givenFacilityType = "양로원"
        def givenLastId = 2L
        def givenPageSize = 20L

        when:
        elderlyFacilityService.readAll(givenFacilityType, givenLastId, givenPageSize)

        then:
        1 * elderlyFacilityReader.readAll(*_) >> {
            String facilityType, Long lastId, Long pageSize ->
                facilityType == givenFacilityType
                lastId == givenLastId
                pageSize == givenPageSize
                List.of(ElderlyFacility.builder().build())
        }
    }

    def "search - elderlyFacilitySearcher 를 호출한다"() {
        given:
        def givenQuery = "구준형 양로원"
        def givenAddress = "인천광역시"
        def givenFacilityType = "양로원"
        def givenGrade = "A"
        def givenMinPrice = (double) 10000
        def givenMaxPrice = (double) 100000
        def givenWithinYears = 1
        def givenPage = 1
        def givenSize = 10

        when:
        elderlyFacilityService.search(givenQuery, givenAddress, givenFacilityType, givenGrade, givenMinPrice, givenMaxPrice, givenWithinYears, givenPage, givenSize)

        then:
        1 * elderlyFacilitySearcher.search(*_) >> {
            String query, String address, String facilityType, String grade, double minPrice, double maxPrice, int withinYears, int page, int size ->
                query == givenQuery
                address == givenAddress
                facilityType == givenFacilityType
                grade == givenGrade
                minPrice == givenMinPrice
                maxPrice == givenMaxPrice
                withinYears == givenWithinYears
                page == givenPage
                size == givenSize
                List.of(ElderlyFacilityDocument.builder().id("1").build())
        }
    }
}
