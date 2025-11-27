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
    private LocalDateTime createdDate;

    @Builder
    private Terms(Long id, TermsType termsType, String version, String title, String content, Boolean isMandatory, Boolean isActive, LocalDateTime createdDate) {
        this.id = id;
        this.termsType = termsType;
        this.version = version;
        this.title = title;
        this.content = content;
        this.isMandatory = isMandatory;
        this.isActive = isActive;
        this.createdDate = createdDate;
    }

    public static Terms create(TermsType termsType, String version, String title, String content, Boolean isMandatory) {
        return Terms.builder()
                .termsType(termsType)
                .version(version)
                .title(title)
                .content(content)
                .isMandatory(isMandatory)
                .isActive(true)
                .createdDate(LocalDateTime.now())
                .build();
    }

    public void deactivate() {
        this.isActive = false;
    }
}
