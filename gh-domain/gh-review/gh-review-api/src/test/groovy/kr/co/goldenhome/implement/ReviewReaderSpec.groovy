package kr.co.goldenhome.implement

import kr.co.goldenhome.FacilityApi
import kr.co.goldenhome.FacilityApiResponse
import kr.co.goldenhome.ReviewImageApi
import kr.co.goldenhome.ReviewImageApiResponse
import kr.co.goldenhome.UserApi
import kr.co.goldenhome.dto.ReviewResponse
import kr.co.goldenhome.entity.Review
import kr.co.goldenhome.repository.ReviewRepository
import spock.lang.Specification

import java.time.LocalDateTime

class ReviewReaderSpec extends Specification {

    ReviewReader reviewReader
    ReviewRepository reviewRepository = Mock()
    UserApi userApi = Mock()
    ReviewImageApi reviewImageApi = Mock()
    FacilityApi facilityApi = Mock()

    def setup() {
        reviewReader = new ReviewReader(reviewRepository, userApi, reviewImageApi, facilityApi)
    }

    def "readAll - 로그인 아이디가 존재하지 않으면 사용자 이름을 '탈퇴한 사용자'라고 내려준다"(){
        given:
        def givenFacilityId = 1L
        def givenLastId = 1L
        def givenLastScore = 4
        def givenPageSize = 10
        def givenSort = "score"
        def expectedResponse = Review.builder().id(1).createdAt(LocalDateTime.now()).build()
        def expectedReviewImageApiResponse = new ReviewImageApiResponse(1L, "d", "a")

        reviewRepository.findAllInfiniteScroll(*_) >> [expectedResponse]
        userApi.getLoginId(_) >> null
        reviewImageApi.getByReviewId(*_) >> [expectedReviewImageApiResponse]

        when:
        List<ReviewResponse> responses = reviewReader.readAll(givenFacilityId, givenLastId, givenLastScore, givenPageSize, givenSort)

        then:
        responses[0].writerName() == "탈퇴한 사용자"

    }

    def "readMine - reviewRepository, facilityApi, userApi, reviewImageApi 를 호출한다"() {
        given:
        def givenUserId = 1L
        def givenLastId = 1L
        def givenPageSize = 1L

        when:
        reviewReader.readMine(givenUserId, givenLastId, givenPageSize)

        then:
        1 * reviewRepository.readMine(givenUserId, givenLastId, givenPageSize) >> [Review.builder().id(1).facilityId(1).createdAt(LocalDateTime.now()).build()]
        1 * facilityApi.get(_) >> new FacilityApiResponse("name", "address")
        1 * userApi.getUserName(givenUserId) >> "username"
        1 * reviewImageApi.getByReviewId(_)
    }

}
