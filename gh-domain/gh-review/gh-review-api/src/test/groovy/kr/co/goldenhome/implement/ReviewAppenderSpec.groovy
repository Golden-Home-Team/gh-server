package kr.co.goldenhome.implement

import kr.co.goldenhome.ReviewImageApi
import kr.co.goldenhome.entity.Review
import kr.co.goldenhome.entity.ReviewStatistic
import kr.co.goldenhome.entity.VisitPurpose
import kr.co.goldenhome.repository.ReviewRepository
import spock.lang.Specification

import java.time.LocalDateTime

class ReviewAppenderSpec extends Specification {

    ReviewAppender reviewAppender
    ReviewRepository reviewRepository = Mock()
    ReviewImageApi reviewImageApi = Mock()
    ReviewStatisticManager reviewStatisticManager = Mock()

    def setup() {
        reviewAppender = new ReviewAppender(reviewRepository, reviewImageApi, reviewStatisticManager)
    }

    def "reviewRepository, reviewStatisticManager 를 호출한다(사진이 존재하면 reviewImageApi 를 호출한다)"() {
        given:
        def givenPositive = "좋은점"
        def givenNegative = "싫은점"
        def givenVisitPurpose = VisitPurpose.COUNSELING
        def givenVisitedAt = LocalDateTime.now().toLocalDate()
        def givenScore = 5
        def givenFileNames = List.of("ff22.jpg")
        def givenFacilityId = 1
        def givenUserId = 1
        def expectedReview = Review.builder().id(1L).build()

        when:
        reviewAppender.write(givenPositive, givenNegative, givenVisitPurpose, givenVisitedAt, givenScore, givenFileNames, givenFacilityId, givenUserId)

        then:
        1 * reviewRepository.save(_) >> expectedReview
        1 * reviewImageApi.saveAll(expectedReview.getId(), givenFileNames)
        1 * reviewStatisticManager.append(*_) >> ReviewStatistic.builder().averageScore(BigDecimal.ONE).build()

    }

    def "reviewRepository, reviewStatisticManager 를 호출한다(사진이 존재하지 않아 reviewImageApi 를 호출하지 않는다)"() {
        given:
        def givenPositive = "좋은점"
        def givenNegative = "싫은점"
        def givenVisitPurpose = VisitPurpose.COUNSELING
        def givenVisitedAt = LocalDateTime.now().toLocalDate()
        def givenScore = 5
        def givenFileNames = new ArrayList()
        def givenFacilityId = 1
        def givenUserId = 1
        def expectedReview = Review.builder().id(1L).build()

        when:
        reviewAppender.write(givenPositive, givenNegative, givenVisitPurpose, givenVisitedAt, givenScore, givenFileNames, givenFacilityId, givenUserId)

        then:
        1 * reviewRepository.save(_) >> expectedReview
        0 * reviewImageApi.saveAll(*_)
        1 * reviewStatisticManager.append(*_) >> ReviewStatistic.builder().averageScore(BigDecimal.ONE).build()
    }

}
