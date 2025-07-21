package kr.co.goldenhome.dto;

public record ReviewResponse(
        int score,
        String username,
        int monthsAgo,
        String content,
        String imageUrl
) {
}
