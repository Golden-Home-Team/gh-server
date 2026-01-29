package kr.co.goldenhome;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ApiHistoryContext {
    private String transactionId;
    private ApiRequest apiRequest;
    private ApiResponse apiResponse;

    public ApiHistoryContext() {
        this.transactionId = UUID.randomUUID().toString();
    }

    public void loggingApiRequest(ApiRequest apiRequest) {
        this.apiRequest = apiRequest;
    }

    public void loggingApiResponse(ApiResponse apiResponse) {
        this.apiResponse = apiResponse;
    }

}
