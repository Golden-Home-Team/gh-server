package kr.co.goldenhome.dto;

import kr.co.goldenhome.ReviewMetaData;

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
        FacilityInfoInnerResponse facilityInfoInnerResponse,
        FacilityStaffInnerResponse facilityStaffInnerResponse,
        List<FacilityPhotoResponse> photoResponses,
        List<FacilityProgramResponse> facilityProgramResponses,
        double averageScore,
        int totalCount,
        int onePointCount,
        int twoPointCount,
        int threePointCount,
        int fourPointCount,
        int fivePointCount,
        boolean isLiked,
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
