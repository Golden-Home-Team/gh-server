package kr.co.goldenhome;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewImageApiTestImpl implements ReviewImageApi {

    @Override
    public void saveAll(Long reviewId, List<String> formattedImageNames) {

    }

    @Override
    public List<ReviewImageApiResponse> getByReviewId(Long reviewId) {
        return List.of();
    }
}
