package kr.co.goldenhome;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewApiTestImpl implements ReviewApi {
    @Override
    public ReviewMetaData getReviewMetaData(Long facilityId) {
        return null;
    }

    @Override
    public List<Long> getTopReviewCountFacilityIds(int page, int size) {
        return List.of();
    }

    @Override
    public List<Long> getHighestRatedFacilityIds(int page, int size) {
        return List.of();
    }

}
