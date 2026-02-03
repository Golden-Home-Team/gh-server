package kr.co.goldenhome;

import io.lettuce.core.dynamic.batch.BatchException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TestController {

    private static final Logger log = LoggerFactory.getLogger("api-history");

    /**
     * FCM 테스트 페이지
     * @return
     */
    @GetMapping("/test")
    public String test(){
        log.error("check error");
        return "test";
    }

    @GetMapping("/test/v1")
    @ResponseBody
    public String v2(){
        log.error("check log error");
        return "test";
    }

    @GetMapping("/test/v2")
    @ResponseBody
    public String v3(){
        throw new BatchException(List.of());
//        return "test";
    }
}
