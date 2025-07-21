package kr.co.goldenhome.service;

import kr.co.goldenhome.implement.ReviewAppender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewAppender reviewAppender;
    private final ReviewReader reviewReader;

    public void write(String content, int score, List<String> formattedFileNames, Long facilityId, Long userId) {
        reviewAppender.write(content, score, formattedFileNames, facilityId, userId);
    }

    public void readAll(Long facilityId, String type) {

    }
}
