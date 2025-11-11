package kr.co.goldenhome.controller;

import jakarta.validation.Valid;
import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.dto.CommonResponse;
import kr.co.goldenhome.dto.NoticeRequest;
import kr.co.goldenhome.dto.NoticeResponse;
import kr.co.goldenhome.entity.Notice;
import kr.co.goldenhome.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    public CommonResponse write(@Valid @RequestBody NoticeRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        noticeService.write(request, userPrincipal.userId());
        return CommonResponse.ok();
    }

    @GetMapping("/{noticeId}")
    public NoticeResponse read(@PathVariable("noticeId") Long noticeId) {
        Notice notice = noticeService.read(noticeId);
        return NoticeResponse.from(notice);
    }

    @GetMapping
    public List<NoticeResponse> readAll(
            @RequestParam(value = "lastId", required = false) Long lastId,
            @RequestParam(value = "pageSize", defaultValue = "20") Long pageSize
    ) {
        return noticeService.readAll(lastId, pageSize).stream().map(NoticeResponse::from).toList();
    }
}
