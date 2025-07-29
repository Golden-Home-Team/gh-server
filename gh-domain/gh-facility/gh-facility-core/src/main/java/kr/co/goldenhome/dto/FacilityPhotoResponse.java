package kr.co.goldenhome.dto;

import kr.co.goldenhome.entity.FacilityPhoto;

public record FacilityPhotoResponse(Long id,
                                    String institutionSymbol,
                                    String type,
                                    String name,
                                    String imageUrl,
                                    String description) {
    public static FacilityPhotoResponse from(FacilityPhoto facilityPhoto) {
        return new FacilityPhotoResponse(
                facilityPhoto.getId(),
                facilityPhoto.getInstitutionSymbol(),
                facilityPhoto.getType(),
                facilityPhoto.getName(),
                facilityPhoto.getImageUrl(),
                facilityPhoto.getDescription()
        );
    }
}
