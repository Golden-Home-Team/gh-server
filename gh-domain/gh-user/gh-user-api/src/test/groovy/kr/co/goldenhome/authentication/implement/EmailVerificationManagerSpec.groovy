package kr.co.goldenhome.authentication.implement

import kr.co.goldenhome.entity.EmailVerification
import kr.co.goldenhome.entity.User
import kr.co.goldenhome.exception.CustomException
import kr.co.goldenhome.exception.ErrorCode
import kr.co.goldenhome.infrastructure.EmailVerificationRepository
import kr.co.goldenhome.infrastructure.MailSender
import kr.co.goldenhome.infrastructure.UserRepository
import spock.lang.Specification

import java.time.LocalDateTime

class EmailVerificationManagerSpec extends Specification {

    EmailVerificationManager emailVerificationManager
    EmailVerificationRepository emailVerificationRepository = Mock()
    MailSender mailSender = Mock()
    UserRepository userRepository = Mock()

    def setup() {
        emailVerificationManager = new EmailVerificationManager(
                emailVerificationRepository,
                mailSender,
                userRepository
        )
    }

    def "create - emailVerificationRepository 를 호출한다"() {
        given:
        def givenContact = "01012345678"

        when:
        emailVerificationManager.create(givenContact)

        then:
        1 * emailVerificationRepository.save(_) >> EmailVerification.create("1")
    }

    def "send - mailSender 를 호출한다"() {
        given:
        def givenContact = "01012345678"
        def givenVerificationCode = "12345678"

        when:
        emailVerificationManager.send(givenContact, givenVerificationCode)

        then:
        1 * mailSender.send(givenContact, _, givenVerificationCode)
    }

    def "confirm - emailVerificationRepository, userRepository 를 호출한다"() {
        given:
        def givenEmailAddress = "abc123@naver.com"
        def givenVerificationCode = "123-abc"

        when:
        emailVerificationManager.confirm(givenEmailAddress, givenVerificationCode)

        then:
        1 * emailVerificationRepository.findTopByEmailAddressAndUsedIsFalseAndExpiresAtAfterOrderByCreatedAtDesc(givenEmailAddress, _)
                >> Optional.of(EmailVerification.builder().verificationCode(givenVerificationCode).emailAddress(givenEmailAddress).build())
        1 * userRepository.findByEmail(_) >> Optional.of(User.builder().createdAt(LocalDateTime.now()).loginId("dfekn123").build())
    }

    def "confirm - 인증코드가 다르다면 예외를 던진다"() {
        given:
        def givenEmailAddress = "abc123@naver.com"
        def givenVerificationCode = "123-abc"
        def wrongVerificationCode = "wrong"

        when:
        emailVerificationManager.confirm(givenEmailAddress, wrongVerificationCode)

        then:
        1 * emailVerificationRepository.findTopByEmailAddressAndUsedIsFalseAndExpiresAtAfterOrderByCreatedAtDesc(givenEmailAddress, _)
                >> Optional.of(EmailVerification.builder().verificationCode(givenVerificationCode).emailAddress(givenEmailAddress).build())

        def e = thrown(CustomException)
        e.errorCode == ErrorCode.INVALID_VERIFICATION_CODE
    }

    def "confirm - 이메일에 해당하는 계정이 없다면 예외를 던진다"() {
        given:
        def givenEmailAddress = "abc123@naver.com"
        def givenVerificationCode = "123-abc"

        when:
        emailVerificationManager.confirm(givenEmailAddress, givenVerificationCode)

        then:
        1 * emailVerificationRepository.findTopByEmailAddressAndUsedIsFalseAndExpiresAtAfterOrderByCreatedAtDesc(givenEmailAddress, _)
                >> Optional.of(EmailVerification.builder().verificationCode(givenVerificationCode).emailAddress(givenEmailAddress).build())
        1 * userRepository.findByEmail(givenEmailAddress) >> Optional.empty()

        def e = thrown(CustomException)
        e.errorCode == ErrorCode.EMAIL_NOT_FOUND

    }
}
