package kr.co.goldenhome.infrastructure

import jakarta.persistence.EntityManager
import kr.co.goldenhome.entity.EmailVerification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.time.LocalDateTime

@ActiveProfiles("test")
@DataJpaTest
class EmailVerificationRepositoryTest extends Specification {

    @Autowired
    EmailVerificationRepository emailVerificationRepository

    @Autowired
    EntityManager entityManager

    def setup() {
        // 각 테스트 메서드 실행 전에 DB 초기화 (테스트 간 독립성 보장)
        emailVerificationRepository.deleteAll()
        entityManager.flush()
        entityManager.clear()
    }

    def "findTopByEmailAddressAndUsedIsFalseAndExpiresAtAfterOrderByCreatedAtDesc - 가장 최근의 유효한 코드를 찾아야 한다"() {
        given:
        def testEmail = "test@example.com"
        def now = LocalDateTime.now()

        // 1. 만료된 코드 (제외되어야 함)
        emailVerificationRepository.save(
                EmailVerification.builder()
                        .emailAddress(testEmail)
                        .verificationCode("111111")
                        .createdAt(now.minusMinutes(10))
                        .expiresAt(now.minusMinutes(1)) // 이미 만료됨
                        .used(false)
                        .build()
        )

        // 2. 이미 사용된 코드 (제외되어야 함)
        emailVerificationRepository.save(
                EmailVerification.builder()
                        .emailAddress(testEmail)
                        .verificationCode("222222")
                        .createdAt(now.minusMinutes(8))
                        .expiresAt(now.plusMinutes(5))
                        .used(true) // 사용됨
                        .build()
        )

        // 3. 더 오래된 활성 코드 (제외되어야 함, 더 최신 코드가 존재)
        emailVerificationRepository.save(
                EmailVerification.builder()
                        .emailAddress(testEmail)
                        .verificationCode("333333")
                        .createdAt(now.minusMinutes(5))
                        .expiresAt(now.plusMinutes(10))
                        .used(false)
                        .build()
        )

        // 4. 가장 최근의 활성 코드 (이 코드를 찾아야 함)
        def expectedCode = "444444"
        def latestActiveVerification = emailVerificationRepository.save(
                EmailVerification.builder()
                        .emailAddress(testEmail)
                        .verificationCode(expectedCode)
                        .createdAt(now.minusMinutes(2)) // 가장 최근
                        .expiresAt(now.plusMinutes(10))
                        .used(false)
                        .build()
        )

        // 다른 이메일의 활성 코드 (제외되어야 함)
        emailVerificationRepository.save(
                EmailVerification.builder()
                        .emailAddress("other@example.com")
                        .verificationCode("555555")
                        .createdAt(now.minusMinutes(1))
                        .expiresAt(now.plusMinutes(10))
                        .used(false)
                        .build()
        )
        entityManager.flush() // DB에 변경사항 반영

        when:
        Optional<EmailVerification> foundVerification = emailVerificationRepository.findTopByEmailAddressAndUsedIsFalseAndExpiresAtAfterOrderByCreatedAtDesc(testEmail, now)

        then:
        foundVerification.isPresent()
        foundVerification.get().getEmailAddress() == testEmail
        foundVerification.get().getVerificationCode() == expectedCode
        foundVerification.get().getId() == latestActiveVerification.getId()
    }

    def "findTopByEmailAddressAndUsedIsFalseAndExpiresAtAfterOrderByCreatedAtDesc - 모든 코드가 만료된 경우 아무것도 찾지 못해야 한다"() {
        given:
        def testEmail = "expired@example.com"
        def now = LocalDateTime.now()

        emailVerificationRepository.save(
                EmailVerification.builder()
                        .emailAddress(testEmail)
                        .verificationCode("111111")
                        .createdAt(now.minusMinutes(10))
                        .expiresAt(now.minusMinutes(1)) // 만료됨
                        .used(false)
                        .build()
        )
        emailVerificationRepository.save(
                EmailVerification.builder()
                        .emailAddress(testEmail)
                        .verificationCode("222222")
                        .createdAt(now.minusMinutes(5))
                        .expiresAt(now.minusMinutes(1)) // 만료됨
                        .used(false)
                        .build()
        )
        entityManager.flush()

        when:
        Optional<EmailVerification> foundVerification = emailVerificationRepository.findTopByEmailAddressAndUsedIsFalseAndExpiresAtAfterOrderByCreatedAtDesc(testEmail, now)

        then:
        !foundVerification.isPresent()
    }

    def "findTopByEmailAddressAndUsedIsFalseAndExpiresAtAfterOrderByCreatedAtDesc - 모든 코드가 사용된 경우 아무것도 찾지 못해야 한다"() {
        given:
        def testEmail = "used@example.com"
        def now = LocalDateTime.now()

        emailVerificationRepository.save(
                EmailVerification.builder()
                        .emailAddress(testEmail)
                        .verificationCode("111111")
                        .createdAt(now.minusMinutes(10))
                        .expiresAt(now.plusMinutes(10))
                        .used(true) // 사용됨
                        .build()
        )
        emailVerificationRepository.save(
                EmailVerification.builder()
                        .emailAddress(testEmail)
                        .verificationCode("222222")
                        .createdAt(now.minusMinutes(5))
                        .expiresAt(now.plusMinutes(10))
                        .used(true) // 사용됨
                        .build()
        )
        entityManager.flush()

        when:
        Optional<EmailVerification> foundVerification = emailVerificationRepository.findTopByEmailAddressAndUsedIsFalseAndExpiresAtAfterOrderByCreatedAtDesc(testEmail, now)

        then:
        !foundVerification.isPresent()
    }

    def "findTopByEmailAddressAndUsedIsFalseAndExpiresAtAfterOrderByCreatedAtDesc - 해당 이메일 주소에 대한 코드가 없는 경우 아무것도 찾지 못해야 한다"() {
        given:
        def testEmail = "nonexistent@example.com"
        def now = LocalDateTime.now()

        // 다른 이메일의 코드만 존재
        emailVerificationRepository.save(
                EmailVerification.builder()
                        .emailAddress("another@example.com")
                        .verificationCode("999999")
                        .createdAt(now.minusMinutes(5))
                        .expiresAt(now.plusMinutes(10))
                        .used(false)
                        .build()
        )
        entityManager.flush()

        when:
        Optional<EmailVerification> foundVerification = emailVerificationRepository.findTopByEmailAddressAndUsedIsFalseAndExpiresAtAfterOrderByCreatedAtDesc(testEmail, now)

        then:
        !foundVerification.isPresent()
    }
}
