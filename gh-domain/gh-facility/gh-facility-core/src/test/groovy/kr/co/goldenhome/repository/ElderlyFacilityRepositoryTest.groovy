package kr.co.goldenhome.repository

import jakarta.persistence.EntityManager
import kr.co.goldenhome.entity.ElderlyFacility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("test")
@DataJpaTest
class ElderlyFacilityRepositoryTest extends Specification {

    @Autowired
    ElderlyFacilityRepository elderlyFacilityRepository

    @Autowired
    EntityManager entityManager

    def "FindAllInfiniteScroll without lastId"() {
        given:
        def givenFacilityType = "양로원"
        def givenLimit = 20L

        when:
        for (i in 0..<10) {
            elderlyFacilityRepository.saveAndFlush(ElderlyFacility.builder().facilityType("양로원").build())
        }
        elderlyFacilityRepository.saveAndFlush(ElderlyFacility.builder().facilityType("경로당").build())
        entityManager.clear()
        def response = elderlyFacilityRepository.findAllInfiniteScroll(givenFacilityType, givenLimit)

        then:
        response.size() == 10
    }

    def "FindAllInfiniteScroll with lastId"() {
        given:
        def givenFacilityType = "양로원"
        def givenLimit = 5L
        def givenLastId = 5L

        when:
        for (i in 0..<10) {
            elderlyFacilityRepository.saveAndFlush(ElderlyFacility.builder().facilityType("양로원").build())
        }
        entityManager.clear()
        def response = elderlyFacilityRepository.findAllInfiniteScroll(givenFacilityType, givenLastId, givenLimit)

        then:
        response.size() == 5
        response.get(0).id == 6L
    }
}


