package kr.co.goldenhome.dto;

import kr.co.goldenhome.ReviewMetaData;

import java.math.BigDecimal;
import java.util.List;

public record FacilityDetailResponse(
        Long id,
        String institutionSymbol,
        String name,
        String facilityType,
        String address,
        String phoneNumber,
        Integer establishmentDate,
        String grade,
        Integer capacity,
        Integer currentTotal,
        Integer currentMale,
        Integer currentFemale,
        Double latitude,
        Double longitude,
        FacilityInfoInnerResponse facilityInfoInnerResponse,
        FacilityStaffInnerResponse facilityStaffInnerResponse,
        List<FacilityPhotoResponse> photoResponses,
        List<FacilityProgramResponse> facilityProgramResponses,
        BigDecimal averageScore,
        Long totalCount,
        Long onePointCount,
        Long twoPointCount,
        Long threePointCount,
        Long fourPointCount,
        Long fivePointCount,
        Boolean isLiked,
        Long viewCount
) {

    public static FacilityDetailResponse of(FacilityDetailServiceResponse facilityDetailServiceResponse, ReviewMetaData reviewMetaData, boolean isLiked, Long viewCount) {
        return new FacilityDetailResponse(
                facilityDetailServiceResponse.id(),
                facilityDetailServiceResponse.institutionSymbol(),
                facilityDetailServiceResponse.name(),
                facilityDetailServiceResponse.facilityType(),
                facilityDetailServiceResponse.address(),
                facilityDetailServiceResponse.phoneNumber(),
                facilityDetailServiceResponse.establishmentDate(),
                facilityDetailServiceResponse.grade(),
                facilityDetailServiceResponse.capacity(),
                facilityDetailServiceResponse.currentTotal(),
                facilityDetailServiceResponse.currentMale(),
                facilityDetailServiceResponse.currentFemale(),
                facilityDetailServiceResponse.latitude(),
                facilityDetailServiceResponse.longitude(),
                facilityDetailServiceResponse.facilityInfoInnerResponse(),
                facilityDetailServiceResponse.facilityStaffInnerResponse(),
                facilityDetailServiceResponse.photoResponses(),
                facilityDetailServiceResponse.facilityProgramResponses(),
                reviewMetaData.averageScore(),
                reviewMetaData.totalCount(),
                reviewMetaData.onePointCount(),
                reviewMetaData.twoPointCount(),
                reviewMetaData.threePointCount(),
                reviewMetaData.fourPointCount(),
                reviewMetaData.fivePointCount(),
                isLiked,
                viewCount
        );
    }
}
