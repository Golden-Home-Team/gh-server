package kr.co.goldenhome.repository

import kr.co.goldenhome.entity.Review
import kr.co.goldenhome.entity.VisitPurpose
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.time.LocalDate

@ActiveProfiles("test")
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReviewRepositorySpec extends Specification {

    @Autowired
    ReviewRepository reviewRepository

    def setup() {
        List<Review> list = new ArrayList<>()
        list.add(Review.create(1L, 1L, 5, false, "좋은시설1 (ID 1)", "멀어", VisitPurpose.COUNSELING, LocalDate.now().minusDays(4))) // ID 1
        list.add(Review.create(1L, 1L, 4, false, "좋은시설2 (ID 2)", "멀어", VisitPurpose.COUNSELING, LocalDate.now().minusDays(3))) // ID 2
        list.add(Review.create(1L, 1L, 3, false, "좋은시설3 (ID 3)", "멀어", VisitPurpose.COUNSELING, LocalDate.now().minusDays(2))) // ID 3
        list.add(Review.create(1L, 1L, 2, false, "좋은시설4 (ID 4)", "멀어", VisitPurpose.COUNSELING, LocalDate.now().minusDays(1))) // ID 4
        list.add(Review.create(1L, 1L, 1, false, "좋은시설5 (ID 5)", "멀어", VisitPurpose.COUNSELING, LocalDate.now())) // ID 5

        reviewRepository.saveAllAndFlush(list)
    }

    def "findAllInfiniteScroll - order by score"() {
        given:
        def lastId = 2L
        def lastScore = 4
        def limit = 3L

        when:
        def list = reviewRepository.findAllInfiniteScroll(1L, lastId, lastScore, limit, "score")

        then:
        list.size() == 3
        list.size() == 3
        list.get(0).id == 3L
        list.get(1).id == 4L
        list.get(2).id == 5L

        println "--- 점수 순 (Score DESC, ID DESC) 다음 페이지 출력 ---"
        list.each { review ->
            println "Review ID: ${review.id}, Score: ${review.score}, Content: ${review.positive}"
        }
    }

    def "findAllInfiniteScroll - order by latest"() {
        given:
        def limit = 3L

        when:
        def list = reviewRepository.findAllInfiniteScroll(1L, null, null, limit, "latest")

        then:
        list.size() == 3


        println "--- 최신순 (ID DESC) 첫 페이지 출력 ---"
        list.each { review ->
            println "Review ID: ${review.id}, Score: ${review.score}, Content: ${review.positive}"
        }
    }

    def "findAllInfiniteScroll - order by score 동점 리뷰가 있을 때"() {
        given:
        reviewRepository.save(Review.create(1L, 1L, 4, false, "동점리뷰 (ID 6)", "굿", VisitPurpose.COUNSELING, LocalDate.now()))


        def lastId = 6L
        def lastScore = 4
        def limit = 3L

        when:
        def list = reviewRepository.findAllInfiniteScroll(1L, lastId, lastScore, limit, "score")

        then:
        list.size() == 3
        list.get(0).id == 2L
        list.get(1).id == 3L
        list.get(2).id == 4L

        println "--- 동점 처리 후 다음 페이지 출력 ---"
        list.each { review ->
            println "Review ID: ${review.id}, Score: ${review.score}, Content: ${review.positive}"
        }
    }

    def "findAllInfiniteScroll - order by latest 마지막 리뷰 조회"() {
        given:

        def lastId = 3L
        def limit = 3L

        when:
        def list = reviewRepository.findAllInfiniteScroll(1L, lastId, null, limit, "latest")

        then:

        list.size() == 2
        list.get(0).id == 2L
        list.get(1).id == 1L

        println "--- 최신순 중간 페이지 출력 (끝 도달 전) ---"
        list.each { review ->
            println "Review ID: ${review.id}, Score: ${review.score}, Content: ${review.positive}"
        }

        when:
        def listAfterEnd = reviewRepository.findAllInfiniteScroll(1L, 1L, null, limit, "latest")

        then:
        listAfterEnd.size() == 0
        println "--- 최신순 마지막 페이지 출력 (데이터 끝) ---"
        println "List Size: ${listAfterEnd.size()}"
    }
}