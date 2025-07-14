package kr.co.goldenhome.service

import kr.co.goldenhome.dto.ResumeCreateRequest
import kr.co.goldenhome.dto.ResumeModifyRequest
import kr.co.goldenhome.entity.Resume
import kr.co.goldenhome.repository.ResumeRepository
import spock.lang.Specification

import java.time.LocalDate

class ResumeServiceSpec extends Specification {

    ResumeService resumeService
    def resumeRepository = Mock(ResumeRepository)

    def setup() {
        resumeService = new ResumeService(resumeRepository)
    }

    def "create - resumeRepository 를 호출한다"() {
        given:
        def givenDateOfBirth = LocalDate.of(2000, 7, 2)
        def givenRequest = new ResumeCreateRequest(
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
        def givenUserId = 1L

        when:
        resumeService.create(givenRequest, givenUserId)

        then:
        1 * resumeRepository.save(*_) >> {
            Resume resume ->
                resume.userId == givenUserId
                resume.dateOfBirth == givenDateOfBirth
                resume
        }
    }

    def "read - resumeRepository 를 호출한다"() {
        given:
        def givenUserId = 1L

        when:
        resumeService.read(givenUserId)

        then:
        1 * resumeRepository.findByUserId(*_) >> {
            Long userId ->
                userId == givenUserId
                Optional.of(Resume.builder().build())
        }
    }

    def "modify - resumeRepository 를 호출한다"() {
        given:
        def givenDateOfBirth = LocalDate.of(2000, 7, 2)
        def givenRequest = new ResumeModifyRequest(
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
        def givenUserId = 1L

        when:
        resumeService.modify(givenRequest, givenUserId)

        then:
        1 * resumeRepository.findByUserId(*_) >> {
            Long userId ->
                userId == givenUserId
                Optional.of(Resume.builder().build())
        }

    }
}
