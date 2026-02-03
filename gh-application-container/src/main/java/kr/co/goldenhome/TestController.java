package kr.co.goldenhome;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TestController {

    private static final Logger log = LoggerFactory.getLogger("api-history");

    /**
     * FCM 테스트 페이지
     * @return
     */
    @GetMapping("/test")
    public String v1(){
        log.error("check error");
        return "test";
    }
}
