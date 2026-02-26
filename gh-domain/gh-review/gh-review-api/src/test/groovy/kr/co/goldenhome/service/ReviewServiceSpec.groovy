package kr.co.goldenhome.service

import kr.co.goldenhome.FacilityEventManger
import kr.co.goldenhome.dto.MyReviewResponse
import kr.co.goldenhome.entity.Review
import kr.co.goldenhome.entity.VisitPurpose
import kr.co.goldenhome.implement.ReviewAppender
import kr.co.goldenhome.implement.ReviewReader
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

class ReviewServiceSpec extends Specification {

    ReviewService reviewService
    ReviewAppender reviewAppender = Mock()
    ReviewReader reviewReader = Mock()

//    FacilityEventManger facilityEventManager = Mock()

    def setup() {
        reviewService = new ReviewService(reviewAppender, reviewReader)
    }

    def 'write - reviewAppender, facilityEventManager 를 호출한다'() {
        given:
        def givenPositive = "시설이 괜찮아요"
        def givenNegative = "멀어요"
        def givenPurpose = VisitPurpose.COUNSELING
        def givenVisitedAt = LocalDate.of(2025, 10, 10)
        def givenScore = 5
        def givenFormattedFileNames = List.of("image1")
        def givenFacilityId = 1L
        def givenUserId = 1L

        when:
        reviewService.write(givenPositive, givenNegative, givenPurpose, givenVisitedAt, givenScore, givenFormattedFileNames, givenFacilityId, givenUserId)

        then:
        1 * reviewAppender.write(*_) >> {
            String positive, String negative, VisitPurpose visitPurpose, LocalDate visitedAt, int score, List<String> formattedFileNames, Long facilityId, Long userId ->
                positive == givenPositive
                negative == givenNegative
                visitPurpose == givenPurpose
                visitedAt == givenVisitedAt
                score == givenScore
                formattedFileNames == givenFormattedFileNames
                facilityId == givenFacilityId
                userId == givenUserId
                new ReviewAppenderWriteResponse(BigDecimal.ONE)
        }
//        1 * facilityEventManager.saveLog(_)
    }

    def "readAll - reviewReader 를 호출한다"() {

        given:
        Long givenFacilityId = 1L
        Long givenLastId = 1L
        Integer givenLastScore = 5
        Long givenPageSize = 10L
        String givenSort = "score"

        when:
        reviewService.readAll(givenFacilityId, givenLastId, givenLastScore, givenPageSize, givenSort, false)

        then:
        1 * reviewReader.readAll(*_) >> {
            Long facilityId, Long lastId, Integer lastScore, Long pageSize, String sort ->
                facilityId == givenFacilityId
                lastId == givenLastId
                lastScore == givenLastScore
                pageSize == givenPageSize
                sort == givenSort
                List.of(Review.builder()
                        .writerId(100L)
                        .positive("테스트 리뷰 내용")
                        .negative("테스트 부정 리뷰 내용")
                        .score(5)
                        .createdAt(LocalDateTime.now().minusMonths(1))
                        .build())
        }

    }

    def "readMine - reviewReader 를 호출한다"() {
        def givenUserId = 1L
        def givenLastId = 2L
        def givenPageSize = 3L

        when:
        reviewService.readMine(givenUserId, givenLastId, givenPageSize)

        then:
        1 * reviewReader.readMine(*_) >> {
            Long userId, Long lastId, Long pageSize ->
                userId == givenUserId
                lastId == givenLastId
                pageSize == givenPageSize
                List.of(MyReviewResponse.builder().build())
        }
    }


}
