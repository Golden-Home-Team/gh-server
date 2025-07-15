package kr.co.goldenhome.service

import kr.co.goldenhome.dto.ResumeSubmissionModifyRequest
import kr.co.goldenhome.dto.ResumeSubmissionResponse
import kr.co.goldenhome.entity.ResumeSubmission
import kr.co.goldenhome.impl.ResumeSubmissionManager
import kr.co.goldenhome.repository.ResumeSubmissionRepository
import spock.lang.Specification

import java.time.LocalDate

class ResumeSubmissionServiceSpec extends Specification {

    ResumeSubmissionService resumeSubmissionService
    def resumeSubmissionManager = Mock(ResumeSubmissionManager)
    def resumeSubmissionRepository = Mock(ResumeSubmissionRepository)

    def setup() {
        resumeSubmissionService = new ResumeSubmissionService(resumeSubmissionManager, resumeSubmissionRepository)
    }

    def "create - resumeSubmissionManager 를 호출한다"() {
        given:
        def givenFacilityId = 1L
        def givenUserId = 1L

        when:
        resumeSubmissionService.create(givenFacilityId, givenUserId)

        then:
        1 * resumeSubmissionManager.create(*_) >> {
            Long facilityId, Long userId ->
                facilityId == givenFacilityId
                userId == givenUserId
        }
    }

    def "read - resumeSubmissionRepository 를 호출한다"() {
        given:
        def givenResumeSubmissionId = 1L
        def givenUserId = 1L
        def expectedResponse = ResumeSubmissionResponse.from(ResumeSubmission.builder().build())

        when:
        resumeSubmissionService.read(givenResumeSubmissionId, givenUserId)

        then:
        1 * resumeSubmissionManager.read(*_) >> {
            Long resumeSubmissionId, Long userId ->
                resumeSubmissionId == givenResumeSubmissionId
                userId == givenUserId
                expectedResponse
        }
    }

    def "readAll - resumeSubmissionRepository 를 호출한다"() {
        given:
        def givenUserId = 1L
        def givenLastId = 1L
        def givenPageSize = 10L
        def expectedResponse = List.of(ResumeSubmission.builder().build())

        when:
        resumeSubmissionService.readAll(givenUserId, givenLastId, givenPageSize)

        then:
        1 * resumeSubmissionRepository.findAllInfiniteScroll(*_) >> {
            Long userId, Long lastId, Long pageSize ->
                userId == givenUserId
                lastId == givenLastId
                pageSize == givenPageSize
                expectedResponse
        }
    }

    def "readAll - lastId가 null 이라면 다른 파라미터를 가진 resumeSubmissionRepository 를 호출한다"() {
        given:
        def givenUserId = 1L
        def givenLastId = null
        def givenPageSize = 10L
        def expectedResponse = List.of(ResumeSubmission.builder().build())

        when:
        resumeSubmissionService.readAll(givenUserId, givenLastId, givenPageSize)

        then:
        1 * resumeSubmissionRepository.findAllInfiniteScroll(*_) >> {
            Long userId, Long pageSize ->
                userId == givenUserId
                pageSize == givenPageSize
                expectedResponse
        }
    }


    def "modify - resumeSubmissionManager 를 호출한다"() {
        given:
        def givenDateOfBirth = LocalDate.of(2000, 7, 2)
        def givenRequest = new ResumeSubmissionModifyRequest(
                "구준형",
                givenDateOfBirth,
                "남",
                "B",
                "허리통증",
                "없음",
                "구머니",
                "01040363457",
                "모"
        )
        def givenResumeSubmissionId = 1L
        def givenUserId = 1L

        when:
        resumeSubmissionService.modify(givenRequest, givenResumeSubmissionId, givenUserId)

        then:
        1 * resumeSubmissionManager.modify(*_) >> {
            ResumeSubmissionModifyRequest request, Long resumeSubmissionId, Long userId ->
                request == givenRequest
                resumeSubmissionId == givenResumeSubmissionId
                userId == givenUserId
        }
    }
}
