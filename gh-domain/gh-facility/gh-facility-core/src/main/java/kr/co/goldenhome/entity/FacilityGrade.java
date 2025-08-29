package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.dto.SqliteGrade;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "facility_grades")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class FacilityGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String institutionSymbol;
    private String evaluationDate;
    private String grade;
    private Double totalScore;
    private Double management;
    private Double environmentSafety;
    private Double rights;
    private Double process;
    private Double result;

    @Builder
    private FacilityGrade(Long id, String institutionSymbol, String evaluationDate, String grade, Double totalScore, Double management, Double environmentSafety, Double rights, Double process, Double result) {
        this.id = id;
        this.institutionSymbol = institutionSymbol;
        this.evaluationDate = evaluationDate;
        this.grade = grade;
        this.totalScore = totalScore;
        this.management = management;
        this.environmentSafety = environmentSafety;
        this.rights = rights;
        this.process = process;
        this.result = result;
    }

    public static FacilityGrade from(SqliteGrade sqliteGrade) {
        return FacilityGrade.builder()
                .institutionSymbol(sqliteGrade.getFacilityId())
                .evaluationDate(sqliteGrade.getEvaluationDate())
                .grade(sqliteGrade.getGrade())
                .totalScore(sqliteGrade.getTotalScore())
                .management(sqliteGrade.getManagement())
                .environmentSafety(sqliteGrade.getEnvironmentSafety())
                .rights(sqliteGrade.getRights())
                .process(sqliteGrade.getProcess())
                .result(sqliteGrade.getResult())
                .build();
    }
}
