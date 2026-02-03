package kr.co.goldenhome;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ApiHistoryFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger("api-history");

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException {
        ContentCachingRequestWrapper cachingRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachingResponse = new ContentCachingResponseWrapper(response);

        try {
            ApiHistoryContextHolder.init();
            MDC.put("transactionId", ApiHistoryContextHolder.get().getTransactionId());
            filterChain.doFilter(cachingRequest, cachingResponse);
        } catch (Exception e) {
            toErrorResponse(cachingResponse);
        } finally {
            ApiHistoryContextHolder.logging(toApiRequest(cachingRequest));
            ApiHistoryContextHolder.logging(toApiResponse(cachingResponse));
            log.info(objectMapper.writeValueAsString(ApiHistoryContextHolder.get()));
            MDC.clear();
            ApiHistoryContextHolder.destroy();
            cachingResponse.copyBodyToResponse();
        }
    }

    private void toErrorResponse(ContentCachingResponseWrapper response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setHeader("Content-Type", "application/json");
    }

    private ApiRequest toApiRequest(ContentCachingRequestWrapper request) {
        String parsedRequest = parseRequest(request);
        Map<String, String> parsedHeaders = parseHeaders(request);
        return new ApiRequest(
                request.getRequestURI(),
                request.getMethod(),
                SensitiveMasker.mask(parsedRequest, objectMapper),
                request.getRemoteAddr(),
                SensitiveMasker.maskMap(parsedHeaders),
                request.getParameterMap()
        );
    }

    private ApiResponse toApiResponse(ContentCachingResponseWrapper response) {
        String parsedResponse = parseResponse(response);
        return new ApiResponse(response.getStatus(), SensitiveMasker.mask(parsedResponse, objectMapper));
    }

    private String parseRequest(ContentCachingRequestWrapper request)  {
        byte[] buf = request.getContentAsByteArray();
        if (buf.length > 0) {
            return new String(buf, StandardCharsets.UTF_8);
        }

        try {
            byte[] inputStreamBytes = request.getInputStream().readAllBytes();
            if (inputStreamBytes.length == 0) {
                return "";
            }
            return new String(inputStreamBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }

    }

    private Map<String, String> parseHeaders(ContentCachingRequestWrapper request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                String values = Collections.list(request.getHeaders(key)).toString();
                headers.put(key, values);
            }
        }

        return headers;
    }

    private String parseResponse(ContentCachingResponseWrapper response) {
        return new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
    }
}
