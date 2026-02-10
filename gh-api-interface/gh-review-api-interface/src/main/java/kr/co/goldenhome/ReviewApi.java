package kr.co.goldenhome;

import java.util.List;

public interface ReviewApi {
    ReviewMetaData getReviewMetaData(Long facilityId);
    List<Long> getTopReviewCountFacilityIds(int page, int size);
    List<Long> getHighestRatedFacilityIds(int page, int size);
}
