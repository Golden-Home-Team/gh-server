package kr.co.goldenhome.dto;

import kr.co.goldenhome.ReviewImageApiResponse;
import kr.co.goldenhome.entity.Review;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Builder
public record ReviewResponse(
        Long writerId,
        String writerName,
        String content,
        Integer score,
        List<ReviewImageApiResponse> reviewImageApiResponses,
        int monthsAgo
) {
    public static ReviewResponse of(Review review, String username, List<ReviewImageApiResponse> reviewImageApiResponses, LocalDateTime now) {
        return ReviewResponse.builder()
                .writerId(review.getWriterId())
                .writerName(username)
                .content(review.getContent())
                .score(review.getScore())
                .reviewImageApiResponses(reviewImageApiResponses)
                .monthsAgo((int)ChronoUnit.MONTHS.between(review.getCreatedAt(), now))
                .build();
    }
}
