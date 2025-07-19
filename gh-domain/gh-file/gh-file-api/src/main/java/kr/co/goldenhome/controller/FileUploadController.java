package kr.co.goldenhome.controller;

import jakarta.validation.Valid;
import kr.co.goldenhome.dto.FileUploadRequest;
import kr.co.goldenhome.dto.FileUploadResponse;
import kr.co.goldenhome.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping
    public List<FileUploadResponse> createPresignedUrlsForUpload(@Valid @RequestBody FileUploadRequest request) {
        return fileUploadService.createPresignedUrlsForUpload(request);
    }
}
