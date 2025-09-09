package kr.co.goldenhome.dto;

import kr.co.goldenhome.ReviewImageApiResponse;
import kr.co.goldenhome.entity.Review;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Builder
public record MyReviewResponse(
        Long writerId,
        String writerName,
        String positive,
        String negative,
        Integer score,
        List<ReviewImageApiResponse> reviewImageApiResponses,
        int monthsAgo,
        Long facilityId,
        String facilityName,
        String facilityAddress

) {
    public static MyReviewResponse of(Review review, String username, List<ReviewImageApiResponse> reviewImageApiResponses, LocalDateTime now, String facilityName, String facilityAddress) {
        return MyReviewResponse.builder()
                .writerId(review.getWriterId())
                .writerName(username)
                .positive(review.getPositive())
                .negative(review.getNegative())
                .score(review.getScore())
                .reviewImageApiResponses(reviewImageApiResponses)
                .monthsAgo((int)ChronoUnit.MONTHS.between(review.getCreatedAt(), now))
                .facilityId(review.getFacilityId())
                .facilityName(facilityName)
                .facilityAddress(facilityAddress)
                .build();
    }
}
