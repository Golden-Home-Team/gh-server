package kr.co.goldenhome.service

import kr.co.goldenhome.ReviewImageApi
import kr.co.goldenhome.UserApi
import kr.co.goldenhome.entity.Review
import kr.co.goldenhome.implement.ReviewAppender
import kr.co.goldenhome.implement.ReviewReader
import spock.lang.Specification

import java.time.LocalDateTime

class ReviewServiceSpec extends Specification {

    ReviewService reviewService
    ReviewAppender reviewAppender = Mock()
    ReviewReader reviewReader = Mock()
    UserApi userApi = Mock()
    ReviewImageApi reviewImageApi = Mock()

    def setup() {
        reviewService = new ReviewService(reviewAppender, reviewReader, userApi, reviewImageApi)
    }

    def 'write - reviewAppender 를 호출한다'() {
        given:
        def givenContent = "시설이 괜찮아요"
        def givenScore = 5
        def givenFormattedFileNames = List.of("image1")
        def givenFacilityId = 1L
        def givenUserId = 1L

        when:
        reviewService.write(givenContent, givenScore, givenFormattedFileNames, givenFacilityId, givenUserId)

        then:
        1 * reviewAppender.write(*_) >> {
            String content, int score, List<String> formattedFileNames, Long facilityId, Long userId ->
                content == givenContent
                score == givenScore
                formattedFileNames == givenFormattedFileNames
                facilityId == givenFacilityId
                userId == givenUserId
        }
    }

    def "readAll - reviewReader 를 호출하고 리스트 사이즈에 따라 userApi, reviewImageApi 를 호출한다"() {

        given:
        Long givenFacilityId = 1L
        Long givenLastId = null
        Integer givenLastScore = null
        Long givenPageSize = 10L
        String givenSort = "score"

        when:
        reviewService.readAll(givenFacilityId, givenLastId, givenLastScore, givenPageSize, givenSort)

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
                        .content("테스트 리뷰 내용")
                        .score(5)
                        .createdAt(LocalDateTime.now().minusMonths(1))
                        .build())
        }

        and:
        1 * userApi.getLoginId(*_)
        1 * reviewImageApi.getByReviewId(*_)

    }


}
