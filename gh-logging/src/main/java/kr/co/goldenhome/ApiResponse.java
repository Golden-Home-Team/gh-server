package kr.co.goldenhome;

public record ApiResponse(
        int status,
        String body
) {
}
