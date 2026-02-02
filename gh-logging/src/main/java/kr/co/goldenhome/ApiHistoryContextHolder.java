package kr.co.goldenhome;

public final class ApiHistoryContextHolder {

    private static final ThreadLocal<ApiHistoryContext> apiHistoryContextThreadLocal = new ThreadLocal<>();

    public static void init() {
        apiHistoryContextThreadLocal.set(new ApiHistoryContext());
    }

    public static void logging(ApiRequest apiRequest) {
        ApiHistoryContext apiHistoryContext = apiHistoryContextThreadLocal.get();
        apiHistoryContext.loggingApiRequest(apiRequest);
    }

    public static void logging(ApiResponse apiResponse) {
        ApiHistoryContext apiHistoryContext = apiHistoryContextThreadLocal.get();
        apiHistoryContext.loggingApiResponse(apiResponse);
    }

    public static void destroy() {
        apiHistoryContextThreadLocal.remove();
    }

    public static ApiHistoryContext get() {
        return apiHistoryContextThreadLocal.get();
    }

    public static boolean isActive() {
        return get() != null;
    }

    public static void set(ApiHistoryContext context) {
        apiHistoryContextThreadLocal.set(context);
    }
}
