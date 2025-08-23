package kr.co.goldenhome;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LikeApiTestImpl implements LikeApi {
    @Override
    public boolean isLiked(Long facilityId, Long userId) {
        return false;
    }

    @Override
    public List<Long> getLikedFacilityIds(Long userId) {
        return List.of();
    }
}
