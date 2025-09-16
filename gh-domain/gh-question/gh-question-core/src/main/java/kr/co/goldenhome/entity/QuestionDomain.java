package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "question_domains")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Builder
    private QuestionDomain(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static QuestionDomain create(String name) {
        return QuestionDomain.builder().name(name).build();
    }
}
