package kr.co.goldenhome.repository;

import kr.co.goldenhome.dto.FacilityCombinedDto;

import java.util.List;

public interface FacilityRepository {
    FacilityCombinedDto read(Long facilityId);

}
