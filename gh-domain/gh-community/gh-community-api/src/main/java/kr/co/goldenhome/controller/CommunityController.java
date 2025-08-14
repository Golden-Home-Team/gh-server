package kr.co.goldenhome.controller;

import auth.UserPrincipal;
import dto.CommonResponse;


import kr.co.goldenhome.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;
    @Value("${GOLDEN_HOME_FRONT_URL}")
    private String goldenHomeFrontUrl;

    /**
     * 프론트 페이지 URI 나오면 추후 수정.
     * 초대수락 버튼 누르러 가는 페이지
     * 코드값을 넘겨주고 그대로 아래 enter 메서드 호출하도록
     */
    // todo API 문서
    @GetMapping("/enter")
    public ResponseEntity<Void> redirectEnter(@RequestParam("code") String code)  {
        String url = UriComponentsBuilder.fromUriString(goldenHomeFrontUrl + "/front-api")
                .queryParam("code", code)
                .build()
                .toUriString();
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, url).build();
    }

    // todo API 문서
    @PostMapping("/enter")
    public CommonResponse enter(@RequestParam("code") String code, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        communityService.enter(code, userPrincipal.userId());
        return CommonResponse.ok();
    }
}
