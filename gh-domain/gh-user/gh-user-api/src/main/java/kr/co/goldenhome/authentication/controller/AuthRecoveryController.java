package kr.co.goldenhome.authentication.controller;

import dto.CommonResponse;
import kr.co.goldenhome.authentication.dto.*;
import kr.co.goldenhome.enums.VerificationType;
import kr.co.goldenhome.authentication.service.AuthRecoveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import validator.EnumValidator;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/recover")
public class AuthRecoveryController {

    private final AuthRecoveryService authRecoveryService;

    @PostMapping("/verification-request")
    public VerificationResponse requestVerification(@RequestBody VerificationRequest request) {
        EnumValidator.validate(VerificationType.class, "type", request.type(), "AccountController.requestVerification");
        return authRecoveryService.requestVerification(request);
    }

    @PostMapping("/verification-confirm")
    public VerificationConfirmResponse confirmVerification(@RequestBody VerificationConfirmRequest request) {
        EnumValidator.validate(VerificationType.class, "type", request.type(), "AccountController.confirmVerification");
        return authRecoveryService.confirm(request);
    }

    @PostMapping("/password")
    public CommonResponse resetPassword(@RequestBody ResetPasswordRequest request) {
        authRecoveryService.resetPassword(request);
        return CommonResponse.ok();
    }
}
