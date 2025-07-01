package kr.co.goldenhome.signup.controller;

import dto.CommonResponse;

import kr.co.goldenhome.signup.dto.SignupRequest;
import kr.co.goldenhome.signup.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/signup")
public class SignupController {

    private final SignupService signUpService;

    @GetMapping("/loginId/duplicated")
    public CommonResponse userExists(@RequestParam("loginId") String loginId) {
        signUpService.isLoginIdDuplicated(loginId);
        return CommonResponse.ok();
    }

    @PostMapping
    public CommonResponse signup(@RequestBody SignupRequest request) {
        signUpService.signup(request);
        return CommonResponse.ok();
    }

}
