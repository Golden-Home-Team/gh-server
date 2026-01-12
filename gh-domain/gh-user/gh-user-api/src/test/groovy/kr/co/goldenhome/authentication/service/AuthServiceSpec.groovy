package kr.co.goldenhome.authentication.service

import kr.co.goldenhome.authentication.dto.FindLoginIdRequest
import kr.co.goldenhome.authentication.dto.ResetEmailRequest
import kr.co.goldenhome.authentication.dto.ResetPhoneNumberRequest
import kr.co.goldenhome.authentication.dto.VerificationConfirmServiceResponse
import kr.co.goldenhome.authentication.implement.VerificationManagerFactory
import kr.co.goldenhome.authentication.dto.ResetPasswordRequest

import kr.co.goldenhome.authentication.dto.VerificationRequest
import kr.co.goldenhome.entity.User
import kr.co.goldenhome.infrastructure.PasswordProcessor
import kr.co.goldenhome.infrastructure.UserRepository
import spock.lang.Specification

import java.time.LocalDateTime

class AuthServiceSpec extends Specification {

    AuthService authRecoveryService
    UserRepository userRepository = Mock()
    VerificationManagerFactory factory = Mock()
    PasswordProcessor passwordProcessor = Mock()

    def "setup"() {
        authRecoveryService = new AuthService(factory, userRepository, passwordProcessor)
    }

    def "requestVerification - Email 타입의 emailVerificationManager 를 호출한다"() {
        given:
        def givenRequest = new VerificationRequest(
                "EMAIL",
                "test@goldenhome.co.kr"
        )

        when:
        authRecoveryService.requestVerification(givenRequest)

        then:
        1 * factory.requestVerification(givenRequest.contact(), givenRequest.type())
    }


    def "resetPassword - factory, passwordProcessor, userRepository 를 호출한다"() {
        given:
        def givenRequest = new ResetPasswordRequest("EMAIL", "dn3i39dk", "test1234", "1234", "1234")
        def expectedResponse = new VerificationConfirmServiceResponse(LocalDateTime.now(), "id123")

        when:
        authRecoveryService.resetPassword(givenRequest)

        then:
        1 * factory.confirm(*_) >> expectedResponse
        1 * passwordProcessor.encode(*_)
        1 * userRepository.findByLoginId(*_) >> Optional.of(User.builder().build())
    }

    def "resetEmail - factory, userRepository 를 호출한다"() {
        given:
        def givenRequest = new ResetEmailRequest("EMAIL", "dn3i39dk", "test1234", "feifne@naver.com")
        def expectedResponse = new VerificationConfirmServiceResponse(LocalDateTime.now(), "id123")

        when:
        authRecoveryService.resetEmail(givenRequest)

        then:
        1 * factory.confirm(*_) >> expectedResponse
        1 * userRepository.findByLoginId(*_) >> Optional.of(User.builder().build())
    }

    def "resetPhoneNumber - factory, userRepository 를 호출한다"() {
        given:
        def givenRequest = new ResetPhoneNumberRequest("EMAIL", "dn3i39dk", "test1234", "01012334322")
        def expectedResponse = new VerificationConfirmServiceResponse(LocalDateTime.now(), "id123")

        when:
        authRecoveryService.resetPhoneNumber(givenRequest)

        then:
        1 * factory.confirm(*_) >> expectedResponse
        1 * userRepository.findByLoginId(*_) >> Optional.of(User.builder().build())
    }

    def "findLoginId - factory, userRepository 를 호출한다"() {
        given:
        def givenRequest = new FindLoginIdRequest("EMAIL", "test1234@naver.com", "01012334322")
        def expectedResponse = new VerificationConfirmServiceResponse(LocalDateTime.now(), "id123")

        when:
        authRecoveryService.findLoginId(givenRequest)

        then:
        1 * factory.confirm(*_) >> expectedResponse
        1 * userRepository.findByLoginId(*_) >> Optional.of(User.builder().build())
    }

}