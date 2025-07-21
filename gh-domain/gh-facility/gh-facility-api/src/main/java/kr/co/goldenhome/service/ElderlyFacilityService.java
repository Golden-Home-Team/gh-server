package kr.co.goldenhome.service;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.dto.ElderlyFacilityResponse;
import kr.co.goldenhome.entity.ElderlyFacility;
import kr.co.goldenhome.entity.ElderlyFacilityDocument;
import kr.co.goldenhome.implement.ElderlyFacilityReader;
import kr.co.goldenhome.implement.ElderlyFacilitySearcher;
import kr.co.goldenhome.repository.ElderlyFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Year;
import java.util.ArrayList;
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
