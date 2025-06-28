package kr.co.goldenhome.signup.controller;

import dto.CommonResponse;
import jakarta.validation.Valid;
import kr.co.goldenhome.entity.EmailVerification;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.signup.dto.SignupRequest;
import kr.co.goldenhome.signup.dto.SignupResponse;
import kr.co.goldenhome.signup.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/signup")
public class SignupController {

    private final SignupService signUpService;

    @PostMapping
    public SignupResponse signup(@Valid @RequestBody SignupRequest signupRequest) {
        signupRequest.validate();
        User user = signUpService.signup(signupRequest.toSignup());
        EmailVerification emailVerification = signUpService.sendEmailForVerification(user);
        return new SignupResponse(emailVerification.getVerificationCode());
    }

    @GetMapping("/verify")
    public CommonResponse verify(@RequestParam("code") String code) {
        signUpService.verify(code);
        return CommonResponse.ok();
    }

}
