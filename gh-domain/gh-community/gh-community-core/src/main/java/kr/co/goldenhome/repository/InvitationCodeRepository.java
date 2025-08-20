package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.InvitationCode;
import kr.co.goldenhome.enums.InvitationCodeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InvitationCodeRepository extends JpaRepository<InvitationCode, Long> {
    Optional<InvitationCode> findByCodeAndStatus(String code, InvitationCodeStatus status);
    @Modifying
    @Query(value = "UPDATE invitation_codes " +
            "SET invitation_code_status = 'EXPIRED' " +
            "WHERE expires_at < CURRENT_TIMESTAMP AND invitation_code_status = 'ACTIVE'", nativeQuery = true)
    void updateExpiredInvitationCodes();

}
