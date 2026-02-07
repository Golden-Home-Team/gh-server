package kr.co.goldenhome;

import java.util.List;

public interface LikeApi {
    boolean isLiked(Long facilityId, Long userId);
    List<Long> getLikedFacilityIds(Long userId);
    List<Long> getTopLikedFacilityIds(int page, int size);
}
