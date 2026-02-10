package kr.co.goldenhome.dto;

public record FacilitySearchResponse(
        Long id, String institutionSymbol, String facilityType, String name, String address,
        Integer establishmentYear, String grade, Integer capacity, Integer currentTotal, String profileUrl,
        Double latitude, Double longitude,
        boolean isLiked, Float avgScore
) {
}
