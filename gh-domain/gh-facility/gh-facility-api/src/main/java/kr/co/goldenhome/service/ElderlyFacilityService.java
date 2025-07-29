package kr.co.goldenhome.service;

import kr.co.goldenhome.dto.ElderlyFacilityResponse;
import kr.co.goldenhome.entity.ElderlyFacility;
import kr.co.goldenhome.entity.ElderlyFacilityDocument;
import kr.co.goldenhome.implement.ElderlyFacilityReader;
import kr.co.goldenhome.implement.ElderlyFacilitySearcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElderlyFacilityService {

    private final ElderlyFacilityReader elderlyFacilityReader;
    private final ElderlyFacilitySearcher elderlyFacilitySearcher;

    public ElderlyFacilityResponse read(Long facilityId) {
        ElderlyFacility elderlyFacility = elderlyFacilityReader.read(facilityId);
        return ElderlyFacilityResponse.from(elderlyFacility);
    }

    public List<ElderlyFacilityResponse> readAll(String facilityType, Long lastId, Long pageSize) {
        List<ElderlyFacility> elderlyFacilities = elderlyFacilityReader.readAll(facilityType, lastId, pageSize);
        return elderlyFacilities.stream().map(ElderlyFacilityResponse::from).toList();
    }

    public List<ElderlyFacilityResponse> search(String query, String address, String facilityType, String grade, double minPrice, double maxPrice, int withinYears, int page, int size) {
        List<ElderlyFacilityDocument> elderlyFacilityDocuments = elderlyFacilitySearcher.search(query, address, facilityType, grade, minPrice, maxPrice, withinYears, page, size);
        return elderlyFacilityDocuments.stream().map(ElderlyFacilityResponse::from).toList();
    }


}
