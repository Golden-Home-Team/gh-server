package kr.co.goldenhome.implement;

import kr.co.goldenhome.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FacilityMetaDataManager {

    private final FacilityProfileApi facilityProfileApi;
    private final LikeApi likeApi;
    private final ReviewApi reviewApi;
    private final ViewApi viewApi;

    public String getProfileUrl(Long facilityId) {
        return facilityProfileApi.get(facilityId);
    }

    public ReviewMetaData getReviewMetaData(Long facilityId) {
        return reviewApi.getReviewMetaData(facilityId);
    }

    public boolean isLiked(Long facilityId, Long userId) {
        return likeApi.isLiked(facilityId, userId);
    }

    public Long view(Long facilityId, Long userId) {
        return viewApi.increase(facilityId, userId);
    }

    public List<Long> getLikedFacilityIds(Long facilityId) {
        return likeApi.getLikedFacilityIds(facilityId);
    }


    public List<Long> getPriorityIds(int page, int size, String sort) {
        return switch (sort) {
            case "like" -> likeApi.getTopLikedFacilityIds(page, size);
            case "view" -> viewApi.getTopViewedFacilityIds(page, size);
            case "review" -> reviewApi.getTopReviewCountFacilityIds(page, size);
            case "highestRated" -> reviewApi.getHighestRatedFacilityIds(page, size);
            default -> null;
        };
    }


}
