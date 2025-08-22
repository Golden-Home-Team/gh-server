package kr.co.goldenhome.service;

import kr.co.goldenhome.LikeApi;
import kr.co.goldenhome.ReviewApi;
import kr.co.goldenhome.ReviewMetaData;
import kr.co.goldenhome.dto.FacilityDetailResponse;
import kr.co.goldenhome.dto.FacilityDetailServiceResponse;
import kr.co.goldenhome.dto.FacilityResponse;
import kr.co.goldenhome.entity.Facility;
import kr.co.goldenhome.entity.FacilityDocument;
import kr.co.goldenhome.implement.FacilityReader;
import kr.co.goldenhome.implement.FacilitySearcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityQueryService {

    private final FacilitySearcher facilitySearcher;
    private final FacilityReader facilityReader;
    private final ReviewApi reviewApi;
    private final LikeApi likeApi;

    public List<FacilityResponse> search(String name, String address, String facilityType, String grade, String sort, int withinYears, int page, int size) {
        List<FacilityDocument> facilityDocuments = facilitySearcher.search(name, address, facilityType, grade, sort, withinYears, page, size);
        return facilityDocuments.stream().map(FacilityResponse::from).toList();
    }

    public FacilityDetailResponse read(Long facilityId, Long userId) {
        FacilityDetailServiceResponse facilityDetailServiceResponse = facilityReader.read(facilityId);
        ReviewMetaData reviewMetaData = reviewApi.getReviewMetaData(facilityId);
        boolean isLiked = likeApi.isLiked(facilityId, userId);
        return FacilityDetailResponse.of(facilityDetailServiceResponse, reviewMetaData, isLiked);
    }

    public List<FacilityResponse> getLikedFacilities(Long userId) {
        List<Long> facilityIds = likeApi.getLikedFacilityIds(userId);
        List<Facility> facilities = facilityReader.getByIds(facilityIds);
        return null;
    }
}
