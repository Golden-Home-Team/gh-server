package kr.co.goldenhome;

import java.util.List;

public interface ViewApi {
    Long increase(Long facilityId, Long userId);
    List<Long> getTopViewedFacilityIds(int page, int size);
}
