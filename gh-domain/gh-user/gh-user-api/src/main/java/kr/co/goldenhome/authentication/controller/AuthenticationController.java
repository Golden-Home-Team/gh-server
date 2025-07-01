package kr.co.goldenhome.authentication.controller;

import auth.UserPrincipal;
import dto.CommonResponse;
import jakarta.validation.Valid;
import kr.co.goldenhome.authentication.dto.LoginRequest;
import kr.co.goldenhome.authentication.dto.LoginResponse;
import kr.co.goldenhome.authentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
