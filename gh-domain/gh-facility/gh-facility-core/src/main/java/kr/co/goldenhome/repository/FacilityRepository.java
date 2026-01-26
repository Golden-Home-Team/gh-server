package kr.co.goldenhome.repository;

import kr.co.goldenhome.dto.FacilityCombinedDto;
import kr.co.goldenhome.entity.Facility;

import java.util.List;

public interface FacilityRepository {
    FacilityCombinedDto read(Long facilityId);
    List<Facility> findByIdIn(List<Long> facilityIds);
    List<Facility> searchByFullTextFallback(String keyword, int page, int size);
    List<Facility> searchByLikeFallback(String keyword, int page, int size);
}
