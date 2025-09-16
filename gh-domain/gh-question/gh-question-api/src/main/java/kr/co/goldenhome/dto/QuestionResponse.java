package kr.co.goldenhome.dto;

import java.util.List;

public record QuestionResponse(
    Long questionDomainId,
    String content,
    List<QuestionDomainOptionInnerResponse> questionDomainOptionInnerResponses
) {
}

