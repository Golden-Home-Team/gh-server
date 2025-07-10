package kr.co.goldenhome.service

import kr.co.goldenhome.entity.ElderlyFacility
import kr.co.goldenhome.repository.ElderlyFacilityRepository
import spock.lang.Specification

class ElderlyFacilityServiceTest extends Specification {

    ElderlyFacilityService elderlyFacilityService
    ElderlyFacilityRepository elderlyFacilityRepository = Mock()

    def setup() {
        elderlyFacilityService = new ElderlyFacilityService(elderlyFacilityRepository)
    }

    def "read - ElderlyFacilityRepository 를 호출한다"() {

        given:
        def givenFacilityId = 1L
        def expectedElderlyFacility = ElderlyFacility.builder().id(1L).build()

        when:
        elderlyFacilityService.read(givenFacilityId)

        then:
        1 * elderlyFacilityRepository.findById(*_) >> {
            Long facilityId ->
                facilityId == givenFacilityId
                Optional.of(expectedElderlyFacility)
        }
    }

    def "readAll - ElderlyFacilityRepository 를 호출한다"() {
        given:
        def givenFacilityType = "양로원"
        def givenLastId = 2L
        def givenPageSize = 20L

        when:
        elderlyFacilityService.readAll(givenFacilityType, givenLastId, givenPageSize)

        then:
        1 * elderlyFacilityRepository.findAllInfiniteScroll(*_) >> {
            String facilityType, Long lastId, Long pageSize ->
                facilityType == givenFacilityType
                lastId == givenLastId
                pageSize == givenPageSize
                List.of(ElderlyFacility.builder().build())
        }
    }
}
