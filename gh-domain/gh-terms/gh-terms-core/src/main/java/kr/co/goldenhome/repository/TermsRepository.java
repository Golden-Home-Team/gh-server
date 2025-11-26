package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.Terms;
import kr.co.goldenhome.entity.TermsType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TermsRepository extends JpaRepository<Terms, Long> {

    Optional<Terms> findByTermsTypeAndIsActiveTrue(TermsType termsType);
    List<Terms> findAllByIsActiveTrue();
}
