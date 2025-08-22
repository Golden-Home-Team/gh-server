package kr.co.goldenhome.dto;


import kr.co.goldenhome.entity.Facility;
import kr.co.goldenhome.entity.FacilityDocument;

public record FacilityResponse(Long id, String institutionSymbol, String facilityType, String name, String address,
                               Integer establishmentYear, String grade, Integer capacity, Integer currentTotal, String profileUrl) {

    public static FacilityResponse from(FacilityDocument facilityDocument, String profileUrl) {
        return new FacilityResponse(
                Long.valueOf(facilityDocument.getId()),
                facilityDocument.getInstitutionSymbol(),
                facilityDocument.getFacilityType(),
                facilityDocument.getName(),
                facilityDocument.getAddress(),
                facilityDocument.getEstablishmentYear(),
                facilityDocument.getGrade(),
                facilityDocument.getCapacity(),
                facilityDocument.getCurrentTotal(),
                profileUrl
        );
    }

    public static FacilityResponse from(Facility facility, String profileUrl) {
        return new FacilityResponse(
                facility.getId(),
                facility.getInstitutionSymbol(),
                facility.getFacilityType(),
                facility.getName(),
                facility.getAddress(),
                facility.getEstablishmentDate(),
                facility.getGrade(),
                facility.getCapacity(),
                facility.getCurrentTotal(),
                profileUrl
        );
    }

}
