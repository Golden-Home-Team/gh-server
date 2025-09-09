package kr.co.goldenhome.dto;

import java.time.LocalDate;
import java.util.List;

public record ReviewRequest(String positive, String negative, String visitPurpose, LocalDate visitedAt, Integer score, List<String> formattedImageNames) {
}
