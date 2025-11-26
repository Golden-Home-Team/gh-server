package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.TermsAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TermsAgreementRepository extends JpaRepository<TermsAgreement, Long> {
    Optional<TermsAgreement> findByTermsIdAndUserId(Long termsId, Long userId);
}
