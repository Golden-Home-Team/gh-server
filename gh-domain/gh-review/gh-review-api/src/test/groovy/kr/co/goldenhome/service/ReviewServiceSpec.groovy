package kr.co.goldenhome.service

import kr.co.goldenhome.implement.ReviewAppender
import spock.lang.Specification

class ReviewServiceSpec extends Specification {

    ReviewService reviewService
    ReviewAppender reviewAppender = Mock()

    def setup() {
        reviewService = new ReviewService(reviewAppender)
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


}
