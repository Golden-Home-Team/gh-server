package kr.co.goldenhome.implement;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.entity.ElderlyFacility;
import kr.co.goldenhome.repository.ElderlyFacilityRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ElderlyFacilityReader {

    private final ElderlyFacilityRepository elderlyFacilityRepository;

    public ElderlyFacility read(Long facilityId) {
        return elderlyFacilityRepository.findById(facilityId).orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND, "ElderlyFacilityService.read"));
    }

    public List<ElderlyFacility> readAll(String facilityType, Long lastId, Long pageSize) {
        return lastId == null ?
                elderlyFacilityRepository.findAllInfiniteScroll(facilityType, pageSize) :
                elderlyFacilityRepository.findAllInfiniteScroll(facilityType, lastId, pageSize);
    }


}
