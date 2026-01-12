package kr.co.goldenhome.signup.controller;

import jakarta.validation.Valid;
import kr.co.goldenhome.dto.CommonResponse;

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
    public CommonResponse checkLoginIdDuplication(@RequestParam("loginId") String loginId) {
        signUpService.isLoginIdDuplicated(loginId);
        return CommonResponse.ok();
    }

    @PostMapping
    public CommonResponse signup(@Valid @RequestBody SignupRequest request) {
        signUpService.signup(request);
        return CommonResponse.ok();
    }

    @GetMapping("/email/duplicated")
    public CommonResponse checkEmailDuplication(@RequestParam("email") String email) {
        signUpService.isEmailDuplicated(email);
        return CommonResponse.ok();
    }

}
