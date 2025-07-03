package exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExternalApiException extends RuntimeException {
    private String errorCode;
    private String errorMessage;
}
