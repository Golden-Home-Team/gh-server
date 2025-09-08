package kr.co.goldenhome;

import kr.co.goldenhome.entity.FacilityLike;
import kr.co.goldenhome.repository.FacilityLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LikeApiImpl implements LikeApi {

    private final FacilityLikeRepository facilityLikeRepository;

    @Override
    public boolean isLiked(Long facilityId, Long userId) {
        return facilityLikeRepository.findByFacilityIdAndUserId(facilityId, userId).isPresent();
    }

    @Override
    public List<Long> getLikedFacilityIds(Long userId) {
        return facilityLikeRepository.findByUserId(userId)
                .stream().map(FacilityLike::getFacilityId).toList();
    }
}
