package kr.co.goldenhome.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.sentry.Sentry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.goldenhome.ApiHistoryContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class NonCustomExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper;
    private static final ErrorResponse errorResponse = new ErrorResponse(500, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    private static final Logger log = LoggerFactory.getLogger("api-history");

    @Override
    public ModelAndView resolveException(@NonNull HttpServletRequest request,
                                         @NonNull HttpServletResponse response,
                                         Object handler,
                                         @NonNull Exception ex) {

        try {
            sendErrorResponse(response);
            log.error("[Error] TID: {}, Message: {}",
                    ApiHistoryContextHolder.get().getTransactionId(),
                    ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ModelAndView();
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.getWriter().flush();
    }
}
