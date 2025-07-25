package kr.co.goldenhome.authentication.controller;

import exception.ExternalApiException;
import jakarta.validation.Valid;
import kr.co.goldenhome.SocialPlatform;
import kr.co.goldenhome.authentication.dto.LoginRequest;
import kr.co.goldenhome.authentication.dto.LoginResponse;
import kr.co.goldenhome.authentication.dto.RefreshRequest;
import kr.co.goldenhome.authentication.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import validator.EnumValidator;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return loginService.login(request);
    }

    @GetMapping("/social/login/initiate")
    public ResponseEntity<Void> initiateSocialLogin(@RequestParam("provider_type") String providerType) {
        EnumValidator.validate(SocialPlatform.class, "providerType", providerType, "AuthenticationController.initiateSocialLogin");
        return loginService.getAuthorizationCode(SocialPlatform.valueOf(providerType));
    }

    @GetMapping("/social/login/callback")
    public LoginResponse socialLoginCallback(@RequestParam("provider_type") String providerType,
                                             @RequestParam(value = "code",required = false) String authorizationCode,
                                             @RequestParam(value = "error", required = false) String errorCode,
                                             @RequestParam(value = "error_description", required = false) String errorDescription) {
        if (errorCode != null) throw new ExternalApiException(errorCode, errorDescription);
        return loginService.getUserInfo(SocialPlatform.valueOf(providerType), authorizationCode);
    }

    @PostMapping("/refresh")
    public LoginResponse tokenRefresh(@Valid @RequestBody RefreshRequest request) {
        return loginService.refresh(request.refreshToken());
    }

}
