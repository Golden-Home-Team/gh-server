package kr.co.goldenhome.account.controller;

import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.dto.CommonResponse;
import kr.co.goldenhome.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
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
}
