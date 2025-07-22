package kr.co.goldenhome.service;

import kr.co.goldenhome.ReviewImageApi;
import kr.co.goldenhome.UserApi;
import kr.co.goldenhome.dto.ReviewResponse;
import kr.co.goldenhome.implement.ReviewAppender;
import kr.co.goldenhome.implement.ReviewReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewAppender reviewAppender;
    private final ReviewReader reviewReader;
    private final UserApi userApi;
    private final ReviewImageApi reviewImageApi;

    public void write(String content, int score, List<String> formattedFileNames, Long facilityId, Long userId) {
        reviewAppender.write(content, score, formattedFileNames, facilityId, userId);
    }

    public List<ReviewResponse> readAll(Long facilityId, Long lastId, Integer lastScore, Long pageSize, String sort) {
        LocalDateTime now = LocalDateTime.now();
        return reviewReader.readAll(facilityId, lastId, lastScore, pageSize, sort)
                .stream().map(review -> ReviewResponse.of(review, userApi.getLoginId(review.getWriterId()), reviewImageApi.getByReviewId(review.getId()), now)).toList();
    }
}
