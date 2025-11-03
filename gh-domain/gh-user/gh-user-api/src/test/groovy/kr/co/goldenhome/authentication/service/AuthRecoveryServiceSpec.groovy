package kr.co.goldenhome.authentication.service

import kr.co.goldenhome.exception.CustomException
import kr.co.goldenhome.authentication.dto.ResetPasswordRequest
import kr.co.goldenhome.authentication.dto.VerificationConfirmRequest
import kr.co.goldenhome.authentication.dto.VerificationConfirmServiceResponse
import kr.co.goldenhome.authentication.dto.VerificationRequest
import kr.co.goldenhome.authentication.implement.VerificationManager
import kr.co.goldenhome.entity.User
import kr.co.goldenhome.enums.VerificationType
import kr.co.goldenhome.infrastructure.PasswordProcessor
import kr.co.goldenhome.infrastructure.ResetPasswordTokenRepository
import kr.co.goldenhome.infrastructure.UserRepository
import spock.lang.Specification

import java.time.LocalDateTime

class AuthRecoveryServiceSpec extends Specification {

    AuthRecoveryService authRecoveryService
    UserRepository userRepository = Mock()
    VerificationManager emailVerificationManager = Mock()
    PasswordProcessor passwordProcessor = Mock()
    ResetPasswordTokenRepository resetPasswordTokenRepository = Mock()

    def "setup"() {
        authRecoveryService = new AuthRecoveryService([emailVerificationManager], userRepository, passwordProcessor, resetPasswordTokenRepository)
        emailVerificationManager.getVerificationType() >> VerificationType.EMAIL
    }

    def "requestVerification - Email 타입의 emailVerificationManager 를 호출한다"() {
        given:
        def givenRequest = new VerificationRequest(
                "EMAIL",
                "test@goldenhome.co.kr"
        )
        def contact = givenRequest.contact()
        def code = "123456"

        emailVerificationManager.create(contact) >> code
        emailVerificationManager.send(contact, code) >> { }

        when:
        authRecoveryService.requestVerification(givenRequest)

        then:
        1 * emailVerificationManager.create(contact)
        1 * emailVerificationManager.send(contact, *_)
    }

    def "confirm - loginId 가 없다면 resetPasswordToken 을 발급하지 않는다"() {
        given:
        def givenRequest = new VerificationConfirmRequest("EMAIL", "gucoding@naver.com", "123456", null)
        def expectedServiceResponse = new VerificationConfirmServiceResponse(LocalDateTime.of(2000, 10, 10,10,10), null)

        when:
        authRecoveryService.confirm(givenRequest)

        then:
        1 * emailVerificationManager.confirm(*_) >> {
            String contact, String verificationCode ->
                contact == givenRequest.contact()
                verificationCode == givenRequest.verificationCode()
                expectedServiceResponse

        }

        0 * resetPasswordTokenRepository.save(*_)

    }

    def "confirm - loginId 가 있다면 resetPasswordToken 을 발급한다"() {
        given:
        def givenRequest = new VerificationConfirmRequest("EMAIL", "gucoding@naver.com", "123456", "gucoding1234")
        def expectedServiceResponse = new VerificationConfirmServiceResponse(LocalDateTime.of(2000, 10, 10,10,10), "gucoding1234")

        when:
        authRecoveryService.confirm(givenRequest)

        then:
        1 * emailVerificationManager.confirm(*_) >> {
            String contact, String verificationCode ->
                contact == givenRequest.contact()
                verificationCode == givenRequest.verificationCode()
                expectedServiceResponse
        }

        1 * resetPasswordTokenRepository.save(*_)

    }

    def "confirm - request.loginId 가 serviceResponse.loginId 와 다르면 예외를 던진다"() {
        given:
        def givenRequest = new VerificationConfirmRequest("EMAIL", "gucoding@naver.com", "123456", "gucoding1234")
        def expectedServiceResponse = new VerificationConfirmServiceResponse(LocalDateTime.of(2000, 10, 10,10,10), "abc1234")

        when:
        authRecoveryService.confirm(givenRequest)

        then:
        1 * emailVerificationManager.confirm(*_) >> {
            String contact, String verificationCode ->
                contact == givenRequest.contact()
                verificationCode == givenRequest.verificationCode()
                expectedServiceResponse
        }
        thrown(CustomException)

    }

    def "resetPassword - passwordProcessor, userRepository, resetPasswordTokenRepository 를 호출한다"() {
        given:
        def givenRequest = new ResetPasswordRequest("dn3i39dk", "test1234", "1234", "1234")

        when:
        authRecoveryService.resetPassword(givenRequest)

        then:
        1 * passwordProcessor.encode(*_)
        1 * userRepository.findByLoginId(*_) >> Optional.of(User.builder().build())
        1 * resetPasswordTokenRepository.getByKey(*_) >> "test1234"
    }

    def "resetPassword - 비밀번호와 비밀번호확인이 다르면 예외를 던진다"() {
        given:
        def givenRequest = new ResetPasswordRequest("dn3i39dk", "test1234", "1234", "12345")

        when:
        authRecoveryService.resetPassword(givenRequest)

        then:
        thrown(CustomException)

    }

    def "resetPassword - 저장되있던 loginId 와 request.logId 가 다르면 예외를 던진다"() {
        given:
        def givenRequest = new ResetPasswordRequest("dn3i39dk", "test1234", "1234", "12345")
        resetPasswordTokenRepository.getByKey(*_) >> "test12345"

        when:
        authRecoveryService.resetPassword(givenRequest)

        then:
        thrown(CustomException)

    }


}