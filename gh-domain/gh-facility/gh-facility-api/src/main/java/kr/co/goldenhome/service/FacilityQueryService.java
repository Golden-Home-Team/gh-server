package kr.co.goldenhome.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.goldenhome.*;
import kr.co.goldenhome.dto.FacilityDetailResponse;
import kr.co.goldenhome.dto.FacilityDetailServiceResponse;
import kr.co.goldenhome.dto.FacilityResponse;
import kr.co.goldenhome.entity.Facility;
import kr.co.goldenhome.entity.FacilityDocument;
import kr.co.goldenhome.implement.FacilityReader;
import kr.co.goldenhome.implement.FacilitySearcher;
import kr.co.goldenhome.FacilityEventManger;
import kr.co.goldenhome.model.FacilityEvent;
import kr.co.goldenhome.model.FacilityEventType;
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
    private final FacilityProfileApi facilityProfileApi;
    private final ViewApi viewApi;
    private final FacilityEventManger facilityEventManger;

    public List<FacilityResponse> search(String name, String address, String facilityType, String grade, String sort, int withinYears, int page, int size) {
        List<FacilityDocument> facilityDocuments = facilitySearcher.search(name, address, facilityType, grade, sort, withinYears, page, size);
        return facilityDocuments.stream().map(document -> {
            String profileUrl = facilityProfileApi.get(Long.valueOf(document.getId()));
            return FacilityResponse.from(document, profileUrl);
        }).toList();
    }

    public FacilityDetailResponse read(Long facilityId, Long userId) {
        FacilityDetailServiceResponse facilityDetailServiceResponse = facilityReader.read(facilityId);
        ReviewMetaData reviewMetaData = reviewApi.getReviewMetaData(facilityId);
        boolean isLiked = likeApi.isLiked(facilityId, userId);
        Long viewCount = viewApi.increase(facilityId, userId);
        facilityEventManger.saveLog(FacilityEvent.create(facilityId, FacilityEventType.VIEW));
        return FacilityDetailResponse.of(facilityDetailServiceResponse, reviewMetaData, isLiked, viewCount);
    }

    public List<FacilityResponse> getLikedFacilities(Long userId) {
        List<Long> facilityIds = likeApi.getLikedFacilityIds(userId);
        List<Facility> facilities = facilityReader.getByIds(facilityIds);
        return facilities.stream().map(facility -> {
            String profileUrl = facilityProfileApi.get(facility.getId());
            return FacilityResponse.from(facility, profileUrl);
        }).toList();
    }
}
