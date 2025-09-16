package kr.co.goldenhome.dto;

import java.util.List;

public record QuestionSurveyRequest(
        List<Long> questionDomainOptionIds
) {
}
