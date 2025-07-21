package kr.co.goldenhome.service;

import kr.co.goldenhome.dto.FileUploadRequest;
import kr.co.goldenhome.dto.FileUploadResponse;
import kr.co.goldenhome.implement.PresignedUrlManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final PresignedUrlManager presignedUrlManager;

    public List<FileUploadResponse> createPresignedUrlsForUpload(FileUploadRequest request) {
        return request.fileNames().stream().map(presignedUrlManager::generatePresignedUrl).toList();
    }
}

