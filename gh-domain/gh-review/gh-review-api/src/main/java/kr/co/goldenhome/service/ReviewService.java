package kr.co.goldenhome.service;

import kr.co.goldenhome.FacilityEventManger;
import kr.co.goldenhome.dto.MyReviewResponse;
import kr.co.goldenhome.dto.ReviewResponse;
import kr.co.goldenhome.entity.VisitPurpose;
import kr.co.goldenhome.implement.ReviewAppender;
import kr.co.goldenhome.implement.ReviewReader;
import kr.co.goldenhome.model.FacilityEvent;
import kr.co.goldenhome.model.FacilityEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewAppender reviewAppender;
    private final ReviewReader reviewReader;
    private final FacilityEventManger facilityEventManger;

    public void write(String positive, String negative, VisitPurpose visitPurpose, LocalDate visitedAt, int score, List<String> formattedFileNames, Long facilityId, Long userId) {
        ReviewAppenderWriteResponse appenderWriteResponse = reviewAppender.write(positive, negative, visitPurpose, visitedAt, score, formattedFileNames, facilityId, userId);
        facilityEventManger.saveLog(FacilityEvent.createReviewEvent(facilityId, FacilityEventType.REVIEW, appenderWriteResponse.averageScore().floatValue()));
    }

    public List<ReviewResponse> readAll(Long facilityId, Long lastId, Integer lastScore, Long pageSize, String sort, boolean hasPhoto) {
        List<ReviewResponse> reviewResponses = reviewReader.readAll(facilityId, lastId, lastScore, pageSize, sort);
        if (hasPhoto) {
            reviewResponses = reviewResponses.stream()
                    .filter(reviewResponse -> !reviewResponse.reviewImageApiResponses().isEmpty())
                    .toList();
        }
        return reviewResponses;
    }

    public List<MyReviewResponse> readMine(Long userId, Long lastId, Long pageSize) {
        return reviewReader.readMine(userId, lastId, pageSize);
    }
}
