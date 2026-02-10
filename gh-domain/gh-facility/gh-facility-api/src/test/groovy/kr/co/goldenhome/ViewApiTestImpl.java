package kr.co.goldenhome;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ViewApiTestImpl implements ViewApi {
    @Override
    public Long increase(Long facilityId, Long userId) {
        return 0L;
    }

    @Override
    public List<Long> getTopViewedFacilityIds(int page, int size) {
        return List.of();
    }
}
