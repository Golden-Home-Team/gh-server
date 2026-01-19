package kr.co.goldenhome.implement;

import kr.co.goldenhome.ReviewImageApi;
import kr.co.goldenhome.entity.Review;
import kr.co.goldenhome.entity.ReviewStatistic;
import kr.co.goldenhome.entity.VisitPurpose;
import kr.co.goldenhome.repository.ReviewRepository;
import kr.co.goldenhome.service.ReviewAppenderWriteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewAppender {

    private final ReviewRepository reviewRepository;
    private final ReviewImageApi reviewImageApi;
    private final ReviewStatisticManager reviewStatisticManager;

    @Transactional
    public ReviewAppenderWriteResponse write(String positive, String negative, VisitPurpose visitPurpose, LocalDate visitedAt, int score, List<String> formattedFileNames, Long facilityId, Long userId) {
        boolean hasPhoto = !formattedFileNames.isEmpty();
        Review review = reviewRepository.save(Review.create(facilityId, userId, score, hasPhoto, positive, negative, visitPurpose, visitedAt));
        if (hasPhoto) reviewImageApi.saveAll(review.getId(), formattedFileNames);
        ReviewStatistic reviewStatistic = reviewStatisticManager.append(facilityId, score);
        return new ReviewAppenderWriteResponse(reviewStatistic.getAverageScore());
    }
}
