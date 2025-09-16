package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "question_domain_options")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionDomainOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long questionDomainId;
    private String name;
    private int originalScore;

    @Builder
    private QuestionDomainOption(Long id, Long questionDomainId, String name, int originalScore) {
        this.id = id;
        this.questionDomainId = questionDomainId;
        this.name = name;
        this.originalScore = originalScore;
    }

    public static QuestionDomainOption create(String name, int originalScore, Long questionDomainId) {
        return QuestionDomainOption.builder().name(name).originalScore(originalScore).questionDomainId(questionDomainId).build();
    }
}
