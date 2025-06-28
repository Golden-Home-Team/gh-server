package kr.co.goldenhome.authentication.controller;

import jakarta.validation.Valid;
import kr.co.goldenhome.authentication.dto.LoginRequest;
import kr.co.goldenhome.authentication.dto.LoginResponse;
import kr.co.goldenhome.authentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

}
