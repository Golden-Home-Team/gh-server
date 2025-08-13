package kr.co.goldenhome;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class CommunityController {

    @PreAuthorize("hasAnyRole('ROLE_FACILITY_ADMIN', 'ROLE_SUPER_ADMIN')")
    @GetMapping
    public String test() {
        return "test";
    }
}
