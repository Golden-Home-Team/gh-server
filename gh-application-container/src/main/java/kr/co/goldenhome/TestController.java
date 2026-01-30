package kr.co.goldenhome;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TestController {

    /**
     * FCM 테스트 페이지
     * @return
     */
    @GetMapping("/test")
    public String v1(){
        return "test";
    }
}
