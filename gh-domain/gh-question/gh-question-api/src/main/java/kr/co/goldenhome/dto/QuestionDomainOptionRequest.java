package kr.co.goldenhome.dto;

public record QuestionDomainOptionRequest(
        Long questionDomainId,
        String name,
        int originalScore
) {
}
