package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "questions")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long questionDomainId;
    private String content;


    @Builder
    private Question(Long id, Long questionDomainId, String content) {
        this.id = id;
        this.questionDomainId = questionDomainId;
        this.content = content;
    }

    public static Question create(Long questionDomainId, String content) {
        return Question.builder()
                .questionDomainId(questionDomainId)
                .content(content)
                .build();
    }
}
