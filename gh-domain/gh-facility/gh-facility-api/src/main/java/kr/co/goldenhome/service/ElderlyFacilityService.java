package kr.co.goldenhome.service;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.dto.ElderlyFacilityResponse;
import kr.co.goldenhome.repository.ElderlyFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElderlyFacilityService {

    private final ElderlyFacilityRepository elderlyFacilityRepository;

    public ElderlyFacilityResponse read(Long facilityId) {
        return ElderlyFacilityResponse.from(elderlyFacilityRepository.findById(facilityId).orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND, "ElderlyFacilityService.read")));
    }
}
