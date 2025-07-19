package kr.co.goldenhome.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record FileUploadRequest(@NotEmpty List<String> fileNames) {
}
