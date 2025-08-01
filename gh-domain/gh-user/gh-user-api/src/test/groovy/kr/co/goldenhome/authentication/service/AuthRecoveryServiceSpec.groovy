package kr.co.goldenhome.authentication.service

import kr.co.goldenhome.authentication.dto.VerificationRequest
import kr.co.goldenhome.authentication.dto.VerificationResponse
import kr.co.goldenhome.authentication.implement.VerificationManager
import kr.co.goldenhome.enums.VerificationPurpose
import kr.co.goldenhome.enums.VerificationType
import kr.co.goldenhome.infrastructure.UserRepository
import spock.lang.Specification

class AuthRecoveryServiceSpec extends Specification {

    AuthRecoveryService authRecoveryService
    UserRepository userRepository = Mock()
    VerificationManager emailVerificationManager = Mock()

    def "setup"() {
        authRecoveryService = new AuthRecoveryService([emailVerificationManager], userRepository)
        emailVerificationManager.getVerificationType() >> VerificationType.EMAIL
    }

    def "requestVerification - loginId가 없으면 VerificationPurpose 는 FIND_ID 이다"() {
        given:
        def givenRequest = new VerificationRequest(
                "EMAIL",
                "test@goldenhome.co.kr",
                ""
        )
        def contact = givenRequest.contact()
        def code = "123456"

        emailVerificationManager.create(contact) >> code
        emailVerificationManager.send(contact, code) >> { }

        when:
        VerificationResponse response = authRecoveryService.requestVerification(givenRequest)

        then:
        0 * userRepository.existsByLoginId(*_)

        and:
        response.purpose() == VerificationPurpose.FIND_ID

        and:
        1 * emailVerificationManager.create(contact)
        1 * emailVerificationManager.send(contact, *_)
    }

    def "requestVerification - loginId가 있으면 VerificationPurpose 는  RESET_PASSWORD 이다"() {
        given:
        def givenRequest = new VerificationRequest(
                "EMAIL",
                "test@goldenhome.co.kr",
                "test1234"
        )
        def contact = givenRequest.contact()
        def code = "123456"

        emailVerificationManager.create(contact) >> code
        emailVerificationManager.send(contact, code) >> { }

        when:
        VerificationResponse response = authRecoveryService.requestVerification(givenRequest)

        then:
        1 * userRepository.existsByLoginId(*_) >> true

        and:
        response.purpose() == VerificationPurpose.RESET_PASSWORD

        and:
        1 * emailVerificationManager.create(contact)
        1 * emailVerificationManager.send(contact, *_)
    }


}