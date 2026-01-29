package kr.co.goldenhome;

import java.util.Map;

public record ApiRequest(
        String url,
        String method,
        String body,
        String clientIp,
        Map<String, String> headers,
        Map<String, String[]> params
) {
}
