package kr.co.goldenhome.implement;

import kr.co.goldenhome.ReviewImageApi;
import kr.co.goldenhome.entity.Review;
import kr.co.goldenhome.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewAppender {

    private final ReviewRepository reviewRepository;
    private final ReviewImageApi reviewImageApi;

    public void write(String content, int score, List<String> formattedFileNames, Long facilityId, Long userId) {
        Review review = reviewRepository.save(Review.create(facilityId, userId, content, score));
        if (!formattedFileNames.isEmpty()) reviewImageApi.saveAll(review.getId(), formattedFileNames);
    }
}
