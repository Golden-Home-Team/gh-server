package kr.co.goldenhome.implement;

import kr.co.goldenhome.entity.ReviewStatistic;
import kr.co.goldenhome.repository.ReviewStatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ReviewStatisticManager {

    private final ReviewStatisticRepository reviewStatisticRepository;

    @Retryable
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ReviewStatistic append(Long facilityId, int score) {
        ReviewStatistic stat = reviewStatisticRepository.findById(facilityId)
                .orElseGet(() -> ReviewStatistic.create(facilityId, 1L, (long) score, BigDecimal.valueOf(score)));
        stat.append(score);
        reviewStatisticRepository.save(stat);
        return stat;
    }
}
