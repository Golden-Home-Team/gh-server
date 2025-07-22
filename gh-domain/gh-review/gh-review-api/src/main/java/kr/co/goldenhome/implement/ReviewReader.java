package kr.co.goldenhome.implement;

import kr.co.goldenhome.entity.Review;
import kr.co.goldenhome.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewReader {

    private final ReviewRepository reviewRepository;

    public List<Review> readAll(Long facilityId, Long lastId, Integer lastScore, Long pageSize, String sort) {
        return reviewRepository.findAllInfiniteScroll(facilityId, lastId, lastScore, pageSize, sort);
    }
}
