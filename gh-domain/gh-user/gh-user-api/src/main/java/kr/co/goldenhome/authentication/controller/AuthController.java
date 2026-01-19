package kr.co.goldenhome.authentication.controller;

import jakarta.validation.Valid;
import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.dto.CommonResponse;
import kr.co.goldenhome.authentication.dto.*;
import kr.co.goldenhome.enums.VerificationType;
import kr.co.goldenhome.authentication.service.AuthService;
import kr.co.goldenhome.validator.DtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import kr.co.goldenhome.validator.EnumValidator;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/verification-request")
    public VerificationResponse requestVerification(@RequestBody VerificationRequest request) {
        EnumValidator.validate(VerificationType.class, "type", request.type(), "Auth.requestVerification");
        return new VerificationResponse(authService.requestVerification(request));
    }

    @PostMapping("/reset-password")
    public CommonResponse resetPassword(@RequestBody @Valid ResetPasswordRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        DtoValidator.password(request.newPassword(), request.confirmPassword());
        authService.resetPassword(request, userPrincipal.userId());
        return CommonResponse.ok();
    }

    @PostMapping("/reset-email")
    public CommonResponse resetEmail(@RequestBody @Valid ResetEmailRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        authService.resetEmail(request, userPrincipal.userId());
        return CommonResponse.ok();
    }

    @PostMapping("/reset-phone")
    public CommonResponse resetPhoneNumber(@RequestBody @Valid ResetPhoneNumberRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        authService.resetPhoneNumber(request, userPrincipal.userId());
        return CommonResponse.ok();
    }

    @PostMapping("/find-login-id")
    public FindLoginIdResponse findLoginId(@RequestBody @Valid FindLoginIdRequest request) {
        return authService.findLoginId(request);
    }

}
