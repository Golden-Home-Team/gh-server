package kr.co.goldenhome.implement;

import kr.co.goldenhome.ReviewImageApi;
import kr.co.goldenhome.entity.Review;
import kr.co.goldenhome.entity.ReviewCount;
import kr.co.goldenhome.repository.ReviewCountRepository;
import kr.co.goldenhome.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewAppender {

    private final ReviewRepository reviewRepository;
    private final ReviewImageApi reviewImageApi;
    private final ReviewCountRepository reviewCountRepository;

    @Transactional
    public void write(String content, int score, List<String> formattedFileNames, Long facilityId, Long userId) {
        boolean hasPhoto = !formattedFileNames.isEmpty();
        Review review = reviewRepository.save(Review.create(facilityId, userId, content, score, hasPhoto));
        if (hasPhoto) reviewImageApi.saveAll(review.getId(), formattedFileNames);
        int result = reviewCountRepository.increase(facilityId);
        if (result == 0) {
            reviewCountRepository.save(ReviewCount.create(facilityId, 1L));
        }
    }
}
