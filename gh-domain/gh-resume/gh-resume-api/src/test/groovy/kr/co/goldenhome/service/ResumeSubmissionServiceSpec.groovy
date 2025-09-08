package kr.co.goldenhome.service

import kr.co.goldenhome.FacilityApi
import kr.co.goldenhome.FacilityApiResponse
import kr.co.goldenhome.entity.ResumeSubmission
import kr.co.goldenhome.submission.implement.ResumeSubmissionReader
import kr.co.goldenhome.submission.implement.ResumeSubmitter
import kr.co.goldenhome.submission.service.ResumeSubmissionService
import spock.lang.Specification

class ResumeSubmissionServiceSpec extends Specification {

    ResumeSubmissionService resumeSubmissionService
    ResumeSubmissionReader resumeSubmissionReader = Mock()
    ResumeSubmitter resumeSubmitter = Mock()
    FacilityApi facilityApi = Mock()


    def setup() {
        resumeSubmissionService = new ResumeSubmissionService(resumeSubmissionReader, resumeSubmitter, facilityApi)
    }

    def 'submit - resumeSubmitter 를 호출한다'() {
        given:
        def givenFacilityId = 1L
        def givenUserId = 1L

        when:
        resumeSubmissionService.submit(givenFacilityId, givenUserId)

        then:
        1 * resumeSubmitter.submit(*_) >> {
            Long facilityId, Long userId ->
                facilityId == givenFacilityId
                userId == givenUserId
        }
    }

    def "read - resumeSubmissionReader, facilityApi 를 호출한다"() {
        given:
        def givenResumeSubmissionId = 1L
        def givenUserId = 1L
        def givenResumeSubmission = ResumeSubmission.builder().id(givenResumeSubmissionId).facilityId(3L).build()
        def givenFacilityResponse = new FacilityApiResponse("", "")

        when:
        resumeSubmissionService.read(givenResumeSubmissionId, givenUserId)

        then:
        1 * resumeSubmissionReader.read(*_) >> {
            Long resumeSubmissionId, Long userId ->
                resumeSubmissionId == givenResumeSubmissionId
                userId == givenUserId
                givenResumeSubmission
        }
        1 * facilityApi.get(givenResumeSubmission.facilityId) >> givenFacilityResponse
    }

    def "readAll - resumeSubmissionReader, facilityApi 를 호출한다"() {
        given:
        def givenUserId = 1L
        def givenLastId = 1L
        def givenPageSize = 10L

        when:
        resumeSubmissionService.readAll(givenUserId, givenLastId, givenPageSize)

        then:
        1 * resumeSubmissionReader.readAll(*_) >> {
            Long userId, Long lastId, Long pageSize ->
                userId == givenUserId
                lastId == givenLastId
                pageSize == givenPageSize
                List.of(ResumeSubmission.builder().facilityId(1L).build())
        }
        1 * facilityApi.get(1L) >> new FacilityApiResponse("", "")

    }

}
