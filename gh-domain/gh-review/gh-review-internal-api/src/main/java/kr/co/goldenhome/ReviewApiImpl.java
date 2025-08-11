package kr.co.goldenhome;


import kr.co.goldenhome.entity.Review;

import kr.co.goldenhome.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewApiImpl implements ReviewApi {

    private final ReviewRepository reviewRepository;

    @Override
    public ReviewMetaData getReviewMetaData(Long facilityId) {
        List<Review> reviews = reviewRepository.findByFacilityId(facilityId);
        if (reviews.isEmpty()) return ReviewMetaData.noData();
        return calculateReviewMetaData(reviews);
    }

    private ReviewMetaData calculateReviewMetaData(List<Review> reviews) {
        int totalScore = 0;
        int[] scoreCounts = new int[6];
        for (Review review : reviews) {
            totalScore += review.getScore();
            scoreCounts[review.getScore()]++;
        }

        double averageScore = (double) totalScore / reviews.size();
        int totalCount = reviews.size();

        return new ReviewMetaData(
                averageScore,
                totalCount,
                scoreCounts[1],
                scoreCounts[2],
                scoreCounts[3],
                scoreCounts[4],
                scoreCounts[5]
        );
    }
}
