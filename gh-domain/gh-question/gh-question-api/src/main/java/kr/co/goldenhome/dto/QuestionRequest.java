package kr.co.goldenhome.dto;

public record QuestionRequest(
        Long questionDomainId,
        String content
) {
}
