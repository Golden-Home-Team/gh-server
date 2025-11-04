package kr.co.goldenhome.account.controller;

import jakarta.validation.Valid;
import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.authentication.dto.FcmRequest;
import kr.co.goldenhome.dto.CommonResponse;
import kr.co.goldenhome.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/withdraw")
    public CommonResponse withdraw(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        accountService.withdraw(userPrincipal.userId());
        return CommonResponse.ok();
    }

    @PostMapping("/logout")
    public CommonResponse logout(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        accountService.logout(userPrincipal.userId());
        return CommonResponse.ok();
    }

    @PostMapping("/fcm")
    public CommonResponse saveFcmToken(@Valid @RequestBody FcmRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        accountService.saveOrUpdateFcmToken(request, userPrincipal.userId());
        return CommonResponse.ok();
    }
}
