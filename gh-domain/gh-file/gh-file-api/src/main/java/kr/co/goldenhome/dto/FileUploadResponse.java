package kr.co.goldenhome.dto;

public record FileUploadResponse(String formattedFileName, String presignedUrl) {
}
