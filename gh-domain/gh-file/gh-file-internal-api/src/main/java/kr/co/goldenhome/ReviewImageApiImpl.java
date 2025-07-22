package kr.co.goldenhome;

import kr.co.goldenhome.entity.ReviewImage;
import kr.co.goldenhome.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewImageApiImpl implements ReviewImageApi {

    private final ReviewImageRepository reviewImageRepository;
    @Value("${aws.s3.base-url}")
    private String awsBaseUrl;

    @Override
    public void saveAll(Long reviewId, List<String> formattedImageNames) {
        List<ReviewImage> reviewImages = formattedImageNames.stream().map(formattedImageName -> ReviewImage.create(reviewId, formattedImageName, awsBaseUrl + formattedImageName)).toList();
        reviewImageRepository.saveAll(reviewImages);
    }

    @Override
    public List<ReviewImageApiResponse> getByReviewId(Long reviewId) {
        return reviewImageRepository.findByReviewId(reviewId)
                .stream().map(reviewImage ->  new ReviewImageApiResponse(reviewImage.getId(), reviewImage.getFormattedName(), reviewImage.getImageUrl())).toList();
    }
}
