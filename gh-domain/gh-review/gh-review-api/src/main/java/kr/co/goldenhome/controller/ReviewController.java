package kr.co.goldenhome.controller;

import auth.UserPrincipal;
import dto.CommonResponse;
import jakarta.validation.Valid;
import kr.co.goldenhome.dto.ReviewRequest;
import kr.co.goldenhome.dto.ReviewResponse;
import kr.co.goldenhome.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import implement.ScoreProcessor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{facilityId}")
    public CommonResponse write(@Valid @RequestBody ReviewRequest request, @PathVariable("facilityId") Long facilityId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        int score = ScoreProcessor.process(request.score());
        reviewService.write(request.content(), score,  request.formattedImageNames(), facilityId, userPrincipal.userId());
        return CommonResponse.ok();
    }

    @GetMapping("/readAll/{facilityId}")
    public List<ReviewResponse> readAll(
            @PathVariable("facilityId") Long facilityId,
            @RequestParam(value = "sort", defaultValue = "score") String sort,
            @RequestParam(value = "lastId", required = false) Long lastId,
            @RequestParam(value = "lastScore", required = false) Integer lastScore,
            @RequestParam(value = "pageSize", defaultValue = "20") Long pageSize) {

        return reviewService.readAll(facilityId, lastId, lastScore, pageSize, sort);
    }

}
