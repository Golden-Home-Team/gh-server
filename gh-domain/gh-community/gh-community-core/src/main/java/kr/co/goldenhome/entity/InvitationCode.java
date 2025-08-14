package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.enums.InvitationCodeStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        name = "invitation_codes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"code"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InvitationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Long facilityId;
    private Long issuedByUserId;
    private Long usedByUserId;
    @Column(name = "invitation_code_status")
    @Enumerated(EnumType.STRING)
    private InvitationCodeStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    @Builder
    private InvitationCode(Long id, String code, Long facilityId, Long issuedByUserId, Long usedByUserId, InvitationCodeStatus status, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.id = id;
        this.code = code;
        this.facilityId = facilityId;
        this.issuedByUserId = issuedByUserId;
        this.usedByUserId = usedByUserId;
        this.status = status;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public static InvitationCode create(String code, Long facilityId, Long issuedByUserId) {
        return InvitationCode.builder()
                .code(code)
                .facilityId(facilityId)
                .issuedByUserId(issuedByUserId)
                .usedByUserId(issuedByUserId)
                .status(InvitationCodeStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();
    }

    public void use(Long userId) {
        this.status = InvitationCodeStatus.USED;
        this.usedByUserId = userId;
    }
}
