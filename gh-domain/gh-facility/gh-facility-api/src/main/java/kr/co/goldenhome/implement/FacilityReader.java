package kr.co.goldenhome.implement;

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

import java.util.List;

@Component
@RequiredArgsConstructor
public class FacilityReader {

    private final FacilityRepository facilityRepository;
    private final FacilityPhotoRepository facilityPhotoRepository;
    private final FacilityProgramRepository facilityProgramRepository;
    private final FacilityGradeRepository facilityGradeRepository;

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

    public List<FacilitySearchResponse> search(String name, String address, String facilityType, String grade, String sort, int withinYears, int page, int size,
                                               Double latitude, Double longitude, Double radiusKm,
                                               List<Long> priorityIds) {
        return facilityRepository.search(name, address, facilityType, grade, sort, withinYears, page, size, latitude, longitude, radiusKm, priorityIds);
    }

}
