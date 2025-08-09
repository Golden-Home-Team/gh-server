package kr.co.goldenhome.repository

import jakarta.persistence.EntityManager
import kr.co.goldenhome.entity.FacilityViewCount
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("test")
@DataJpaTest
class FacilityViewCountBackUpRepositorySpec extends Specification {

    @Autowired
    FacilityViewCountBackUpRepository facilityViewCountBackUpRepository

    @Autowired
    EntityManager entityManager

    def "updateViewCountTest"() {
        given:
        facilityViewCountBackUpRepository.saveAndFlush(
                FacilityViewCount.create(1L, 0L)
        )
        entityManager.clear()

        when:
        def result1 = facilityViewCountBackUpRepository.updateViewCount(1L, 100L)
        def result2 = facilityViewCountBackUpRepository.updateViewCount(1L, 300L)
        def result3 = facilityViewCountBackUpRepository.updateViewCount(1L, 200L)

        then:
        result1 == 1
        result2 == 1
        result3 == 0
    }
}
