package kr.co.goldenhome.service;

import kr.co.goldenhome.*;
import kr.co.goldenhome.auth.UserPrincipal;
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
    private final FacilityEventManger facilityEventManger;
    private final LikeApi likeApi;

    public List<FacilityResponse> search(String name, String address, String facilityType, String grade, String sort, int withinYears, int page, int size,
                                         Double latitude, Double longitude, Double radiusKm,
                                         UserPrincipal userPrincipal) {
        List<FacilityDocument> facilityDocuments = facilitySearcher.search(name, address, facilityType, grade, sort, withinYears, page, size, latitude, longitude, radiusKm);
        if (userPrincipal == null) {
            return facilityDocuments.stream().map(document -> {
                String profileUrl = facilityReader.getProfileUrl(Long.valueOf(document.getId()));
                return FacilityResponse.of(document, profileUrl);
            }).toList();
        }
        Long userId = userPrincipal.userId();
        return facilityDocuments.stream().map(document -> {
            String profileUrl = facilityReader.getProfileUrl(Long.valueOf(document.getId()));
            boolean isLiked = likeApi.isLiked(Long.valueOf(document.getId()), userId);
            return FacilityResponse.of(document, profileUrl, isLiked);
        }).toList();

    }

    public FacilityDetailResponse read(Long facilityId, Long userId) {
        FacilityDetailServiceResponse facilityDetailServiceResponse = facilityReader.read(facilityId);
        ReviewMetaData reviewMetaData = facilityReader.getReviewMetaData(facilityId);
        boolean isLiked = facilityReader.isLiked(facilityId, userId);
        Long viewCount = facilityReader.view(facilityId, userId);
        facilityEventManger.saveLog(FacilityEvent.create(facilityId, FacilityEventType.VIEW));
        return FacilityDetailResponse.of(facilityDetailServiceResponse, reviewMetaData, isLiked, viewCount);
    }

    public List<FacilityResponse> getLikedFacilities(Long userId) {
        List<Long> facilityIds = facilityReader.getLikedFacilityIds(userId);
        List<Facility> facilities = facilityReader.getByIds(facilityIds);
        return facilities.stream().map(facility -> {
            String profileUrl = facilityReader.getProfileUrl(facility.getId());
            String grade = facilityReader.getGradeByInstitutionSymbol(facility.getInstitutionSymbol());
            return FacilityResponse.getLikedFacilities(facility, profileUrl, grade);
        }).toList();
    }
}
