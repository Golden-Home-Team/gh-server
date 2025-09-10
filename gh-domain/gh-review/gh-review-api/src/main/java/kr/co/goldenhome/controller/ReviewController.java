package kr.co.goldenhome.controller;

import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.dto.CommonResponse;
import jakarta.validation.Valid;
import kr.co.goldenhome.dto.MyReviewResponse;
import kr.co.goldenhome.dto.ReviewRequest;
import kr.co.goldenhome.dto.ReviewResponse;
import kr.co.goldenhome.entity.VisitPurpose;
import kr.co.goldenhome.service.ReviewService;
import kr.co.goldenhome.validator.EnumValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import kr.co.goldenhome.implement.ScoreProcessor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{facilityId}")
    public CommonResponse write(@Valid @RequestBody ReviewRequest request, @PathVariable("facilityId") Long facilityId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        EnumValidator.validate(VisitPurpose.class, "visitPurpose", request.visitPurpose(), "ReviewController.write");
        int score = ScoreProcessor.process(request.score());
        reviewService.write(request.positive(), request.negative(), VisitPurpose.valueOf(request.visitPurpose()), request.visitedAt(), score,  request.formattedImageNames(), facilityId, userPrincipal.userId());
        return CommonResponse.ok();
    }

    @GetMapping("/readAll/{facilityId}")
    public List<ReviewResponse> readAll(
            @PathVariable("facilityId") Long facilityId,
            @RequestParam(value = "sort", defaultValue = "score") String sort,
            @RequestParam(value = "lastId", required = false) Long lastId,
            @RequestParam(value = "lastScore", required = false) Integer lastScore,
            @RequestParam(value = "pageSize", defaultValue = "20") Long pageSize,
            @RequestParam(value = "hasPhoto", defaultValue = "false") boolean hasPhoto) {
        return reviewService.readAll(facilityId, lastId, lastScore, pageSize, sort, hasPhoto);
    }

    @GetMapping("/readAll")
    public List<MyReviewResponse> readMine(
            @RequestParam(value = "lastId", required = false) Long lastId,
            @RequestParam(value = "pageSize", defaultValue = "20") Long pageSize,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return reviewService.readMine(userPrincipal.userId(), lastId, pageSize);
    }

}
