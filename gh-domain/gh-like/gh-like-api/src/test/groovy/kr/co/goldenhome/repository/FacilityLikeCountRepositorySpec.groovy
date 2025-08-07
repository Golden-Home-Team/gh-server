package kr.co.goldenhome.repository

import jakarta.persistence.EntityManager
import kr.co.goldenhome.entity.FacilityLikeCount
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification

@ActiveProfiles("test")
@DataJpaTest
class FacilityLikeCountRepositorySpec extends Specification {

    @Autowired
    FacilityLikeCountRepository facilityLikeCountRepository

    @Autowired
    EntityManager entityManager

    def "increase - 존재하지 않으면 0 을 리턴"() {
        when:
        def result = facilityLikeCountRepository.increase(1L)

        then:
        result == 0
    }

    @Sql(scripts = "classpath:sql/clear_facility_counts.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    def "increase - 존재하면 값 하나 증가"() {
        given:
        facilityLikeCountRepository.saveAndFlush(FacilityLikeCount.create(1L))
        entityManager.clear()

        when:
        def result = facilityLikeCountRepository.increase(1L)


        then:
        result == 1
    }

}
