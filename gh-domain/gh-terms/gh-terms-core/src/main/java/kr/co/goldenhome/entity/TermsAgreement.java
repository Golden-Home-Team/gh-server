package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "terms_agreement")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long termsId;
    private Boolean isAgreed;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder
    private TermsAgreement(Long id, Long userId, Long termsId, Boolean isAgreed, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.userId = userId;
        this.termsId = termsId;
        this.isAgreed = isAgreed;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public static TermsAgreement create(Long userId, Long termsId, Boolean isAgreed) {
        return TermsAgreement.builder()
                .userId(userId)
                .termsId(termsId)
                .isAgreed(isAgreed)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
    }

    public void update(Boolean isAgreed) {
        this.isAgreed = isAgreed;
        this.updatedDate = LocalDateTime.now();
    }
}
