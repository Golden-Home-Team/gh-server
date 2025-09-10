package kr.co.goldenhome.repository

import kr.co.goldenhome.entity.Review
import kr.co.goldenhome.entity.VisitPurpose
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.time.LocalDate

@ActiveProfiles("test")
@DataJpaTest
class ReviewRepositorySpec extends Specification {

    @Autowired
    ReviewRepository reviewRepository

    def setup() {
        List<Review> list = new ArrayList<>()
        list.add(Review.create(1L, 1L, 1, false, "좋은시설1", "멀어", VisitPurpose.COUNSELING, LocalDate.now()))
        list.add(Review.create(1L, 1L, 1, false, "좋은시설2", "멀어", VisitPurpose.COUNSELING, LocalDate.now()))
        list.add(Review.create(1L, 1L, 1, false, "좋은시설3", "멀어", VisitPurpose.COUNSELING, LocalDate.now()))
        list.add(Review.create(1L, 1L, 1, false, "좋은시설4", "멀어", VisitPurpose.COUNSELING, LocalDate.now()))
        list.add(Review.create(1L, 1L, 1, false, "좋은시설5", "멀어", VisitPurpose.COUNSELING, LocalDate.now()))

        reviewRepository.saveAllAndFlush(list)
    }

    def "findAllInfiniteScroll - order by score"() {

        when:
        def list = reviewRepository.findAllInfiniteScroll(1L, 4L, 4, 3L, "score")

        then:
        println "--- 리스트 값 하나씩 출력 (each) ---"
        list.each { review ->
            println "Review ID: ${review.id}, Score: ${review.score}, Content: ${review.positive}"
        }
        println "--- 출력 종료 ---"
    }

    def "findAllInfiniteScroll - order by desc"() {

        when:
        def list = reviewRepository.findAllInfiniteScroll(1L, null, null, 3L, "?")

        then:
        println "--- 리스트 값 하나씩 출력 (each) ---"
        list.each { review ->
            println "Review ID: ${review.id}, Score: ${review.score}, Content: ${review.positive}"
        }
        println "--- 출력 종료 ---"
    }
}
