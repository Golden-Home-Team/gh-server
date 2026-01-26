package kr.co.goldenhome.authentication.controller;

import kr.co.goldenhome.exception.ExternalApiException;
import jakarta.validation.Valid;
import kr.co.goldenhome.SocialPlatform;
import kr.co.goldenhome.authentication.dto.LoginRequest;
import kr.co.goldenhome.authentication.dto.LoginResponse;
import kr.co.goldenhome.authentication.dto.RefreshRequest;
import kr.co.goldenhome.authentication.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import kr.co.goldenhome.validator.EnumValidator;
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity<Void> initiateSocialLogin(@RequestParam("provider_type") String providerType,
                                                    @RequestParam("callback") String frontendRedirectUrl) {
        EnumValidator.validate(SocialPlatform.class, "providerType", providerType, "AuthenticationController.initiateSocialLogin");
        return loginService.getAuthorizationCode(SocialPlatform.valueOf(providerType), frontendRedirectUrl);
    }

    @GetMapping("/social/login/callback")
    public ResponseEntity<Void> socialLoginCallback(
                                             @RequestParam("provider_type") String providerType,
                                             @RequestParam(value = "code",required = false) String authorizationCode,
                                             @RequestParam(value = "error", required = false) String errorCode,
                                             @RequestParam(value = "error_description", required = false) String errorDescription,
                                             @RequestParam(value = "state") String frontendRedirectUrl) {
        if (errorCode != null) throw new ExternalApiException(errorCode, errorDescription);
        LoginResponse response = loginService.getUserInfo(SocialPlatform.valueOf(providerType), authorizationCode);
        String finalUrl = UriComponentsBuilder.fromUriString(frontendRedirectUrl)
                .queryParam("accessToken", response.accessToken())
                .queryParam("refreshToken", response.refreshToken())
                .build()
                .toUriString();
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, finalUrl).build();
    }

    @PostMapping("/refresh")
    public LoginResponse tokenRefresh(@Valid @RequestBody RefreshRequest request) {
        return loginService.refresh(request.refreshToken());
    }

}
