package kr.co.goldenhome.implement;

import kr.co.goldenhome.*;
import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.dto.*;
import kr.co.goldenhome.entity.FacilityGrade;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.entity.Facility;
import kr.co.goldenhome.repository.FacilityGradeRepository;
import kr.co.goldenhome.repository.FacilityPhotoRepository;
import kr.co.goldenhome.repository.FacilityProgramRepository;
import kr.co.goldenhome.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FacilityReader {

    private final FacilityRepository facilityRepository;
    private final FacilityPhotoRepository facilityPhotoRepository;
    private final FacilityProgramRepository facilityProgramRepository;
    private final FacilityGradeRepository facilityGradeRepository;
    private final FacilityProfileApi facilityProfileApi;
    private final LikeApi likeApi;
    private final ReviewApi reviewApi;
    private final ViewApi viewApi;

    public FacilityDetailServiceResponse read(Long facilityId) {
        FacilityCombinedDto facilityCombinedDto = facilityRepository.read(facilityId);
        if (facilityCombinedDto == null) {
            throw new CustomException(ErrorCode.FACILITY_NOT_FOUND, "FacilityReader.read");
        }
        List<FacilityPhotoResponse> facilityPhotoResponses = facilityPhotoRepository.findByInstitutionSymbol(facilityCombinedDto.institutionSymbol())
                .stream().map(FacilityPhotoResponse::from).toList();
        List<FacilityProgramResponse> facilityProgramResponses = facilityProgramRepository.findByInstitutionSymbol(facilityCombinedDto.institutionSymbol())
                .stream().map(FacilityProgramResponse::from).toList();
        return FacilityDetailServiceResponse.of(facilityCombinedDto, facilityPhotoResponses, facilityProgramResponses);
    }

    public List<Facility> getByIds(List<Long> facilityIds) {
        return facilityRepository.findByIdIn(facilityIds);
    }

    public String getGradeByInstitutionSymbol(String institutionSymbol) {
        FacilityGrade facilityGrade = facilityGradeRepository.findTopByInstitutionSymbolOrderByEvaluationDateDesc(institutionSymbol);
        return facilityGrade == null ? null : facilityGrade.getGrade();
    }

    public String getProfileUrl(Long facilityId) {
        return facilityProfileApi.get(facilityId);
    }

    public ReviewMetaData getReviewMetaData(Long facilityId) {
        return reviewApi.getReviewMetaData(facilityId);
    }

    public boolean isLiked(Long facilityId, Long userId) {
        return likeApi.isLiked(facilityId, userId);
    }

    public Long view(Long facilityId, Long userId) {
        return viewApi.increase(facilityId, userId);
    }

    public List<Long> getLikedFacilityIds(Long facilityId) {
        return likeApi.getLikedFacilityIds(facilityId);
    }

    public List<Facility> search(String name, String address, int page, int size) {
        String rawKeyword = StringUtils.hasText(name) ? name : address;
        if (!StringUtils.hasText(rawKeyword)) return Collections.emptyList();
        String cleanKeyword = rawKeyword.replace(" ", "");
        List<Facility> results = facilityRepository.searchByFullTextFallback(cleanKeyword, page, size);
        if (results.isEmpty()) return facilityRepository.searchByLikeFallback(cleanKeyword, page, size);
        return results;
    }

    public List<Facility> search(String name, String address, String facilityType, String grade, String sort, int withinYears, int page, int size,
                                 Double latitude, Double longitude, Double radiusKm,
                                 List<Long> priorityIds) {
        return facilityRepository.search(name, address, facilityType, grade, sort, withinYears, page, size, latitude, longitude, radiusKm, priorityIds);
    }
}
