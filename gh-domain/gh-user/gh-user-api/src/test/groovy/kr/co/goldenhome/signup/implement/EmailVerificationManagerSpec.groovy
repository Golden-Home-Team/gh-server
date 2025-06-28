package kr.co.goldenhome.signup.implement

import kr.co.goldenhome.dto.Email
import kr.co.goldenhome.entity.EmailVerification
import kr.co.goldenhome.enums.EmailVerificationType
import kr.co.goldenhome.infrastructure.EmailVerificationRepository
import kr.co.goldenhome.infrastructure.MailSender
import spock.lang.Specification

class EmailVerificationManagerSpec extends Specification {

    EmailVerificationManager emailVerificationManager
    def emailVerificationRepository = Mock(EmailVerificationRepository)
    def mailSender = Mock(MailSender)

    def setup() {
        emailVerificationManager = new EmailVerificationManager(emailVerificationRepository, mailSender)
    }

    def "create - EmailVerificationRepository.save() 를 호출한다"() {

        given:
        def givenUserId = 1L
        def givenEmailAddress = "gucoding@naver.com"
        def givenEmailVerificationType = EmailVerificationType.SIGN_UP

        when:
        emailVerificationManager.create(givenUserId, givenEmailAddress, givenEmailVerificationType)

        then:
        1 * emailVerificationRepository.save(*_) >> {
            EmailVerification emailVerification ->
                emailVerification.userId == givenUserId
                emailVerification.emailAddress == givenEmailAddress
                emailVerification.emailVerificationType == givenEmailVerificationType
                emailVerification
        }

    }

    def "sendEmail - MailSender.send() 를 호출하고 회원가입 상황이면 그에 맞는 제목의 Email 이 생성된다"() {
        given:
        def givenEmailVerification = EmailVerification.builder()
                .emailAddress("gucoding@naver.com")
                .verificationCode("givenVerificationCode")
                .emailVerificationType(EmailVerificationType.SIGN_UP)
                .build()

        when:
        emailVerificationManager.sendEmail(givenEmailVerification)

        then:
        1 * mailSender.send(*_) >> {
            Email email ->
                email.to == givenEmailVerification.emailAddress
                email.subject == "골든홈 회원가입 인증 메일입니다."
        }
    }

    def "verify - EmailVerificationRepository 를 호출한다" () {
        given:
        def givenCode = "givenCode"
        def expectedEmailVerification = EmailVerification.builder().userId(1L).verificationCode(givenCode).build()

        when:
        emailVerificationManager.verify(givenCode)

        then:
        1 * emailVerificationRepository.findByVerificationCode(givenCode) >> {
            String verificationCode ->
                verificationCode == givenCode
                Optional.of(expectedEmailVerification)
        }

    }



}
