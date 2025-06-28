package kr.co.goldenhome.infrastructure;

import kr.co.goldenhome.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByVerificationCode(String verificationCode);
}
