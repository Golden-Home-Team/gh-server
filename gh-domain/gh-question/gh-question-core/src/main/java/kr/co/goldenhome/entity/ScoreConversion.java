package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
        name = "score_conversions",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_question_domain_id_original_sum", columnNames = {"question_domain_id", "original_sum"})
        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScoreConversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long questionDomainId;
    private int originalSum;
    private double convertedSum;

    @Builder
    private ScoreConversion(Long id, Long questionDomainId, int originalSum, double convertedSum) {
        this.id = id;
        this.questionDomainId = questionDomainId;
        this.originalSum = originalSum;
        this.convertedSum = convertedSum;
    }

    public static ScoreConversion create(Long questionDomainId, int originalSum, double convertedSum) {
        return ScoreConversion.builder()
                .questionDomainId(questionDomainId)
                .originalSum(originalSum)
                .convertedSum(convertedSum)
                .build();
    }
}
