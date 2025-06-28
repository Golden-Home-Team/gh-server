package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.enums.EmailVerificationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Table(name = "email_verifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String emailAddress;
    private String verificationCode;
    @Enumerated(EnumType.STRING)
    private EmailVerificationType emailVerificationType;
    private Boolean isVerified;

    @Builder
    private EmailVerification(Long id, Long userId, String emailAddress, String verificationCode, EmailVerificationType emailVerificationType, Boolean isVerified) {
        this.id = id;
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.verificationCode = verificationCode;
        this.emailVerificationType = emailVerificationType;
        this.isVerified = isVerified;
    }

    public static EmailVerification create(Long userId, String emailAddress, EmailVerificationType type) {
        return EmailVerification.builder()
                .userId(userId)
                .emailAddress(emailAddress)
                .verificationCode(UUID.randomUUID().toString())
                .emailVerificationType(type)
                .build();
    }

    public void verify() {
        this.isVerified = true;
    }
}
