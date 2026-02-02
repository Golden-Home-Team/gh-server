package kr.co.goldenhome.service;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import kr.co.goldenhome.*;
import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.dto.FacilityDetailResponse;
import kr.co.goldenhome.dto.FacilityDetailServiceResponse;
import kr.co.goldenhome.dto.FacilityResponse;
import kr.co.goldenhome.entity.Facility;
import kr.co.goldenhome.entity.FacilityDocument;
import kr.co.goldenhome.entity.RecentView;
import kr.co.goldenhome.implement.FacilityReader;
import kr.co.goldenhome.implement.FacilitySearcher;
import kr.co.goldenhome.FacilityEventManger;
import kr.co.goldenhome.model.FacilityEvent;
import kr.co.goldenhome.model.FacilityEventType;
import kr.co.goldenhome.repository.RecentViewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityQueryService {

    private final FacilitySearcher facilitySearcher;
    private final FacilityReader facilityReader;
    private final FacilityEventManger facilityEventManger;
    private final RecentViewRepository recentViewRepository;
    private final LikeApi likeApi;
    private static final Logger log = LoggerFactory.getLogger("api-history");

    @CircuitBreaker(name = "openSearch", fallbackMethod = "searchFallback")
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

    public List<FacilityResponse> searchFallback(String name, String address, String facilityType, String grade, String sort, int withinYears, int page, int size,
                                                 Double latitude, Double longitude, Double radiusKm,
                                                 UserPrincipal userPrincipal, Throwable throwable) {
        if (throwable instanceof CallNotPermittedException) {
            return handleOpenCircuit(name,address,page,size,userPrincipal);
        }
        return handleException(name,address,page,size,userPrincipal,throwable);
    }

    public FacilityDetailResponse read(Long facilityId, Long userId) {
        FacilityDetailServiceResponse facilityDetailServiceResponse = facilityReader.read(facilityId);
        ReviewMetaData reviewMetaData = facilityReader.getReviewMetaData(facilityId);
        boolean isLiked = facilityReader.isLiked(facilityId, userId);
        Long viewCount = facilityReader.view(facilityId, userId);
        facilityEventManger.saveLog(FacilityEvent.createViewEvent(facilityId, FacilityEventType.VIEW));
        return FacilityDetailResponse.of(facilityDetailServiceResponse, reviewMetaData, isLiked, viewCount);
    }

    public List<FacilityResponse> getLikedFacilities(Long userId) {
        List<Long> facilityIds = facilityReader.getLikedFacilityIds(userId);
        return getFacilityResponses(facilityIds);
    }

    public List<FacilityResponse> recent(Long userId) {
        List<Long> facilityIds = recentViewRepository.findByUserId(userId).stream()
                .map(RecentView::getFacilityId)
                .toList();
        return getFacilityResponses(facilityIds);

    }

    private List<FacilityResponse> handleOpenCircuit(String name, String address, int page, int size, UserPrincipal userPrincipal) {
        log.warn("[FacilityQueryService] Circuit breaker is open. fall back to RDB");
        return getFacilityResponses(name, address, page, size, userPrincipal);
    }

    private List<FacilityResponse> handleException(String name, String address, int page, int size, UserPrincipal userPrincipal, Throwable throwable) {
        log.error("[FacilityQueryService] An error occurred while searching facilities. errorMessage={}", throwable.getMessage());
        return getFacilityResponses(name, address, page, size, userPrincipal);
    }

    private List<FacilityResponse> getFacilityResponses(String name, String address, int page, int size, UserPrincipal userPrincipal) {
        List<Facility> facilities = facilityReader.search(name, address, page, size);
        if (userPrincipal == null) {
            return facilities.stream().map(facility -> {
                String profileUrl = facilityReader.getProfileUrl(facility.getId());
                return FacilityResponse.of(facility, profileUrl);
            }).toList();
        }
        Long userId = userPrincipal.userId();
        return facilities.stream().map(facility -> {
            String profileUrl = facilityReader.getProfileUrl(facility.getId());
            boolean liked = likeApi.isLiked(facility.getId(), userId);
            return FacilityResponse.of(facility, profileUrl, liked);
        }).toList();
    }

    private List<FacilityResponse> getFacilityResponses(List<Long> facilityIds) {
        List<Facility> facilities = facilityReader.getByIds(facilityIds);
        return facilities.stream().map(facility -> {
            String profileUrl = facilityReader.getProfileUrl(facility.getId());
            String grade = facilityReader.getGradeByInstitutionSymbol(facility.getInstitutionSymbol());
            return FacilityResponse.getLikedFacilities(facility, profileUrl, grade);
        }).toList();
    }


//    public List<FacilityRecommendResponse> recommendFacilities(String userQuery) throws IOException {
//        List<Float> queryVector = embeddingClient.getBatchEmbeddings(List.of(userQuery)).getFirst();
//        return embeddingClient.getFacilitiesWithKNN(queryVector);
//    }
}
