package kr.co.goldenhome.dto;


import kr.co.goldenhome.entity.FacilityDocument;

public record FacilityResponse(Long id, String institutionSymbol, String facilityType, String name, String address,
                               Integer establishmentYear, String grade, Integer capacity, Integer currentTotal) {

    public static FacilityResponse from(FacilityDocument facilityDocument) {
        return new FacilityResponse(
                Long.valueOf(facilityDocument.getId()),
                facilityDocument.getInstitutionSymbol(),
                facilityDocument.getFacilityType(),
                facilityDocument.getName(),
                facilityDocument.getAddress(),
                facilityDocument.getEstablishmentYear(),
                facilityDocument.getGrade(),
                facilityDocument.getCapacity(),
                facilityDocument.getCurrentTotal()
        );
    }
}
