package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "email_verifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String emailAddress;
    private String verificationCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean used;

    @Builder
    private EmailVerification(Long id, String emailAddress, String verificationCode, LocalDateTime createdAt, LocalDateTime expiresAt, boolean used) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.verificationCode = verificationCode;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.used = used;
    }

    public static EmailVerification create(String emailAddress) {
        return EmailVerification.builder()
                .emailAddress(emailAddress)
                .verificationCode(String.format("%06d", new SecureRandom().nextInt(1_000_000)))
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();
    }

    public void markAsUsed() {
        this.used = true;
    }

}
