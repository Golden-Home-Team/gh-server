package kr.co.goldenhome.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqliteGrade {
    private String facilityId;
    private String evaluationDate;
    private String grade;
    private Double totalScore;
    private Double management;
    private Double environmentSafety;
    private Double rights;
    private Double process;
    private Double result;

}
