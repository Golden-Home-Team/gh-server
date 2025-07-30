package kr.co.goldenhome.implement;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.dto.FacilityCombinedDto;
import kr.co.goldenhome.dto.FacilityDetailResponse;
import kr.co.goldenhome.dto.FacilityPhotoResponse;
import kr.co.goldenhome.dto.FacilityProgramResponse;
import kr.co.goldenhome.entity.FacilityPhoto;
import kr.co.goldenhome.entity.FacilityProgram;
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

    public FacilityDetailResponse read(Long facilityId) {
        FacilityCombinedDto facilityCombinedDto = facilityRepository.read(facilityId);
        if (facilityCombinedDto == null) {
            throw new CustomException(ErrorCode.FACILITY_NOT_FOUND, "FacilityReader.read");
        }
        List<FacilityPhotoResponse> facilityPhotoResponses = facilityPhotoRepository.findByInstitutionSymbol(facilityCombinedDto.institutionSymbol())
                .stream().map(FacilityPhotoResponse::from).toList();
        List<FacilityProgramResponse> facilityProgramResponses = facilityProgramRepository.findByInstitutionSymbol(facilityCombinedDto.institutionSymbol())
                .stream().map(FacilityProgramResponse::from).toList();
        return FacilityDetailResponse.of(facilityCombinedDto, facilityPhotoResponses, facilityProgramResponses);

    }
}
