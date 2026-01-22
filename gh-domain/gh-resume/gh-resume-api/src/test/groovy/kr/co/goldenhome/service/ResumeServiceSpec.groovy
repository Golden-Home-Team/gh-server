package kr.co.goldenhome.service

import kr.co.goldenhome.entity.ResumePhysicalCondition
import kr.co.goldenhome.enums.AdmissionTimeFrame
import kr.co.goldenhome.enums.Gender
import kr.co.goldenhome.enums.HealthInsurance
import kr.co.goldenhome.enums.LongTermCareGrade
import kr.co.goldenhome.enums.PhysicalCondition
import kr.co.goldenhome.enums.Relationship
import kr.co.goldenhome.resume.dto.ResumeCreateRequest
import kr.co.goldenhome.resume.dto.ResumeModifyRequest
import kr.co.goldenhome.entity.Resume
import kr.co.goldenhome.resume.dto.ResumeResponse
import kr.co.goldenhome.resume.implement.ResumeModifier
import kr.co.goldenhome.resume.implement.ResumeReader
import kr.co.goldenhome.resume.implement.ResumeWriter
import kr.co.goldenhome.resume.service.ResumeService
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
                Gender.MALE.name(),
                List.of(PhysicalCondition.DEMENTIA.name()),
                LongTermCareGrade.GRADE_1.name(),
                HealthInsurance.MEDICAL_AID_TYPE_1.name(),
                "없음",
                "구머니",
                "01040363457",
                Relationship.ETC.name(),
                "양로원",
                AdmissionTimeFrame.IMMEDIATELY.name(),
                "친구"
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
        def expectedResponse = ResumeResponse.of(
                Resume.builder()
                .id(1)
                .userId(1)
                .name("")
                .dateOfBirth(LocalDate.now())
                .build(),
                List.of(ResumePhysicalCondition
                        .builder()
                        .build()
                )
        )

        when:
        resumeService.read(givenUserId)

        then:
        1 * resumeReader.read(*_) >> expectedResponse
    }

    def "modify - resumeModifier 를 호출한다"() {
        given:
        def givenDateOfBirth = LocalDate.of(2000, 7, 2)
        def givenRequest = new ResumeModifyRequest(
                "구준형",
                givenDateOfBirth,
                Gender.MALE.name(),
                List.of(PhysicalCondition.DEMENTIA.name()),
                LongTermCareGrade.GRADE_1.name(),
                HealthInsurance.MEDICAL_AID_TYPE_1.name(),
                "없음",
                "구머니",
                "01040363457",
                Relationship.CHILD.name(),
                "양로원",
                AdmissionTimeFrame.IMMEDIATELY.name(),
                ""
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
