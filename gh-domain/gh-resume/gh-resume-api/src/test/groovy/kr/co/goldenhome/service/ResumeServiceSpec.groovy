package kr.co.goldenhome.service

import kr.co.goldenhome.resume.dto.ResumeCreateRequest
import kr.co.goldenhome.resume.dto.ResumeModifyRequest
import kr.co.goldenhome.entity.Resume
import kr.co.goldenhome.repository.ResumeRepository
import kr.co.goldenhome.resume.implement.ResumeModifier
import kr.co.goldenhome.resume.implement.ResumeReader
import kr.co.goldenhome.resume.implement.ResumeWriter
import kr.co.goldenhome.resume.service.ResumeService
import kr.co.goldenhome.submission.implement.ResumeSubmitter
import spock.lang.Specification

import java.time.LocalDate

class ResumeServiceSpec extends Specification {

    ResumeService resumeService
    ResumeWriter resumeWriter = Mock()
    ResumeReader resumeReader = Mock()
    ResumeModifier resumeModifier = Mock()

    def setup() {
        resumeService = new ResumeService(resumeWriter, resumeReader, resumeModifier)
    }

    def 'write - resumeWriter 를 호출한다'() {
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
        resumeService.write(givenRequest, givenUserId)

        then:
        1 * resumeWriter.write(*_) >> {
            ResumeCreateRequest request, Long userId ->
                userId == givenUserId
                request.dateOfBirth() == givenDateOfBirth
        }
    }

    def "read - resumeReader 를 호출한다"() {
        given:
        def givenUserId = 1L

        when:
        resumeService.read(givenUserId)

        then:
        1 * resumeReader.read(*_) >> {
            Long userId ->
                userId == givenUserId
                Optional.of(Resume.builder().build())
                Resume.builder().build()
        }
    }

    def "modify - resumeModifier 를 호출한다"() {
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
        1 * resumeModifier.modify(*_) >> {
            ResumeModifyRequest request, Long userId ->
                userId == givenUserId
                Optional.of(Resume.builder().build())
        }

    }
}
