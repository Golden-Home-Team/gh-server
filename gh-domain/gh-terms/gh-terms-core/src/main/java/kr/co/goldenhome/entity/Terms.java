package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "terms")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Terms {

    // todo 회원가입 시, 띄울생각! UserStatus PENDING 하고 약관 동의 페이지로 리다이렉트 소셜로그인도 분기처리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TermsType termsType;
    private String version;
    private String title;
    private String content;
    private Boolean isMandatory;
    private Boolean isActive;
    private LocalDateTime createdAt;

    @Builder
    private Terms(Long id, TermsType termsType, String version, String title, String content, Boolean isMandatory, Boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.termsType = termsType;
        this.version = version;
        this.title = title;
        this.content = content;
        this.isMandatory = isMandatory;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public static Terms create(TermsType termsType, String version, String title, String content, Boolean isMandatory) {
        return Terms.builder()
                .termsType(termsType)
                .version(version)
                .title(title)
                .content(content)
                .isMandatory(isMandatory)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void deactivate() {
        this.isActive = false;
    }
}
