package kr.co.goldenhome.implement;


import kr.co.goldenhome.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PresignedUrlManager {

    @Value("${aws.s3.bucket}")
    private String bucket;

    private final S3Presigner s3Presigner;
    private static final Duration presignedUrlExpiration = Duration.ofMinutes(5);

    public FileUploadResponse generatePresignedUrl(String fileName) {
        String formattedFileName = UUID.randomUUID() + "-" + URLEncoder.encode(fileName, StandardCharsets.UTF_8);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(formattedFileName)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(presignedUrlExpiration)
                .putObjectRequest(putObjectRequest)
                .build();

        String presignedUrl = s3Presigner.presignPutObject(presignRequest).url().toString();

        return new FileUploadResponse(formattedFileName, presignedUrl);
    }
}
