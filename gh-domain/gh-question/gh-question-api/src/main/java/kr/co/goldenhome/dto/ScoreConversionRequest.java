package kr.co.goldenhome.dto;

public record ScoreConversionRequest(
        Long questionDomainId,
        int originalSum,
        double convertedSum
) {
}
