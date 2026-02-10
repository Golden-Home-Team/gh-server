package kr.co.goldenhome;


import kr.co.goldenhome.entity.ReviewStatistic;
import kr.co.goldenhome.repository.ReviewStatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewApiImpl implements ReviewApi {

    private final ReviewStatisticRepository reviewStatisticRepository;

    @Override
    public ReviewMetaData getReviewMetaData(Long facilityId) {
        Optional<ReviewStatistic> reviewStatisticOptional = reviewStatisticRepository.findById(facilityId);
        if (reviewStatisticOptional.isEmpty()) return ReviewMetaData.noData();
        ReviewStatistic reviewStatistic = reviewStatisticOptional.get();
        long totalCount = reviewStatistic.getScore1Count()+ reviewStatistic.getScore2Count()+reviewStatistic.getScore3Count()+reviewStatistic.getScore4Count()+reviewStatistic.getScore5Count();
        return new ReviewMetaData(reviewStatistic.getAverageScore(), totalCount, reviewStatistic.getScore1Count(), reviewStatistic.getScore2Count(), reviewStatistic.getScore3Count(), reviewStatistic.getScore4Count(), reviewStatistic.getScore5Count());
    }

    @Override
    public List<Long> getTopReviewCountFacilityIds(int page, int size) {
        return reviewStatisticRepository.findTopReviewCountFacilityIds(PageRequest.of(page-1, size));
    }

    @Override
    public List<Long> getHighestRatedFacilityIds(int page, int size) {
        return reviewStatisticRepository.findTopReviewAvgScoreFacilityIds(PageRequest.of(page-1, size));
    }
}
