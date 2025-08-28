package kr.co.goldenhome;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.entity.Facility;
import kr.co.goldenhome.repository.FacilityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FacilityApiImpl implements FacilityApi {

    private final FacilityJpaRepository facilityJpaRepository;

    @Override
    public FacilityApiResponse get(Long facilityId) {
        Facility facility = facilityJpaRepository.findById(facilityId).orElseThrow(() -> new CustomException(ErrorCode.FACILITY_NOT_FOUND, "FacilityApiImpl.get"));
        return new FacilityApiResponse(facility.getName(), facility.getAddress());
    }
}
