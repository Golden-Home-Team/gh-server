package kr.co.goldenhome.implement;

import kr.co.goldenhome.FacilityApi;
import kr.co.goldenhome.FacilityApiResponse;
import kr.co.goldenhome.ReviewImageApi;
import kr.co.goldenhome.UserApi;
import kr.co.goldenhome.dto.MyReviewResponse;
import kr.co.goldenhome.dto.ReviewResponse;
import kr.co.goldenhome.entity.Review;
import kr.co.goldenhome.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewReader {

    private final ReviewRepository reviewRepository;
    private final UserApi userApi;
    private final ReviewImageApi reviewImageApi;
    private final FacilityApi facilityApi;

    public List<ReviewResponse> readAll(Long facilityId, Long lastId, Integer lastScore, Long pageSize, String sort) {
        List<Review> reviews = reviewRepository.findAllInfiniteScroll(facilityId, lastId, lastScore, pageSize, sort);
        return reviews.stream().map(review -> ReviewResponse.of(review, userApi.getLoginId(review.getWriterId()), reviewImageApi.getByReviewId(review.getId()), LocalDateTime.now())).toList();
    }

    public List<MyReviewResponse> readMine(Long userId, Long lastId, Long pageSize) {
        List<Review> reviews = lastId == null ? reviewRepository.readMine(userId, pageSize) : reviewRepository.readMine(userId, lastId, pageSize);
        return reviews .stream().map(review -> {
                    FacilityApiResponse facilityApiResponse = facilityApi.get(review.getFacilityId());
                    return MyReviewResponse.of(review, userApi.getUserName(userId), reviewImageApi.getByReviewId(review.getId()), LocalDateTime.now(), facilityApiResponse.name(), facilityApiResponse.address());
                }).toList();
    }
}
