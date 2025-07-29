package kr.co.goldenhome.service;

import kr.co.goldenhome.dto.FacilityDetailResponse;
import kr.co.goldenhome.dto.FacilityResponse;
import kr.co.goldenhome.entity.FacilityDocument;
import kr.co.goldenhome.implement.FacilityReader;
import kr.co.goldenhome.implement.FacilitySearcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilitySearcher facilitySearcher;
    private final FacilityReader facilityReader;

    public List<FacilityResponse> search(String name, String address, String facilityType, String grade, String sort, int withinYears, int page, int size) {
        List<FacilityDocument> facilityDocuments = facilitySearcher.search(name, address, facilityType, grade, sort, withinYears, page, size);
        return facilityDocuments.stream().map(FacilityResponse::from).toList();
    }

    public FacilityDetailResponse read(Long facilityId) {
        return facilityReader.read(facilityId);
    }
}
