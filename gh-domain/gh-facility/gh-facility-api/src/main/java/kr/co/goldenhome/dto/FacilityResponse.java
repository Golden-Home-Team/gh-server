package kr.co.goldenhome.dto;


import kr.co.goldenhome.entity.Facility;
import kr.co.goldenhome.entity.FacilityDocument;

public record FacilityResponse(Long id, String institutionSymbol, String facilityType, String name, String address,
                               Integer establishmentYear, String grade, Integer capacity, Integer currentTotal, String profileUrl,
                               Double latitude, Double longitude,
                               boolean isLiked) {

    public static FacilityResponse of(FacilityDocument facilityDocument, String profileUrl) {
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
                profileUrl,
                facilityDocument.getLocation().getLat(), facilityDocument.getLocation().getLon(),
                false
        );
    }

    public static FacilityResponse of(FacilityDocument facilityDocument, String profileUrl, boolean isLiked) {
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
                profileUrl,
                facilityDocument.getLocation().getLat(), facilityDocument.getLocation().getLon(),
                isLiked
        );
    }


    public static FacilityResponse getLikedFacilities(Facility facility, String profileUrl, String grade) {
        return new FacilityResponse(
                facility.getId(),
                facility.getInstitutionSymbol(),
                facility.getFacilityType(),
                facility.getName(),
                facility.getAddress(),
                facility.getEstablishmentDate(),
                grade,
                facility.getCapacity(),
                facility.getCurrentTotal(),
                profileUrl,
                facility.getLatitude(), facility.getLongitude(),
                true
        );
    }

}
