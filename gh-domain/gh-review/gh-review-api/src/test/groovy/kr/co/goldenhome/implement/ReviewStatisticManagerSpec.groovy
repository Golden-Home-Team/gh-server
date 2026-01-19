package kr.co.goldenhome.implement

import kr.co.goldenhome.entity.ReviewStatistic
import kr.co.goldenhome.repository.ReviewStatisticRepository
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.retry.annotation.EnableRetry
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@EnableRetry
@SpringBootTest
@ActiveProfiles("test")
class ReviewStatisticManagerSpec extends Specification {

    @Autowired
    ReviewStatisticManager reviewStatisticManager

    @SpringBean
    ReviewStatisticRepository reviewStatisticRepository = Mock()

    def "낙관적 락 예외 발생 시 지정된 횟수만큼 재시도한다"() {
        given:
        def facilityId = 1L
        def score = 5

        when:
        reviewStatisticManager.append(facilityId, score)

        then:
        // 1~2회차에는 예외 발생, 3회차에 성공하도록 설정
        2 * reviewStatisticRepository.findById(facilityId) >> {
            throw new ObjectOptimisticLockingFailureException(ReviewStatistic.class, facilityId)
        }
        then:
        1 * reviewStatisticRepository.findById(facilityId) >> Optional.of(ReviewStatistic.create(facilityId, 1L, 5L, BigDecimal.valueOf(5)))
        1 * reviewStatisticRepository.save(_)
    }

    def "재시도 횟수를 초과하면 최종적으로 예외를 던진다"() {
        given:
        def facilityId = 1L
        def score = 5

        when:
        reviewStatisticManager.append(facilityId, score)

        then:
        // 기본 설정이 3회라면, 3번 모두 예외 발생 시 예외가 밖으로 던져짐
        3 * reviewStatisticRepository.findById(facilityId) >> {
            throw new ObjectOptimisticLockingFailureException(ReviewStatistic.class, facilityId)
        }
        thrown(ObjectOptimisticLockingFailureException)
    }

}
