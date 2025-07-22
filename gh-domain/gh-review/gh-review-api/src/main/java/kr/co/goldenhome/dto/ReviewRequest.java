package kr.co.goldenhome.dto;

import java.util.List;

public record ReviewRequest(String content, Integer score, List<String> formattedImageNames) {
}
