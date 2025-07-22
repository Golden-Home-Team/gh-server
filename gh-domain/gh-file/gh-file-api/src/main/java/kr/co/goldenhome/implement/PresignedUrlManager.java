package kr.co.goldenhome.implement;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import kr.co.goldenhome.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PresignedUrlManager {

    @Value("${aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 s3Client;
    private static final long presignedUrlExpirationMillis = 5 * 60 * 1000;

    public FileUploadResponse generatePresignedUrl(String fileName) {
        String formattedFileName = UUID.randomUUID() + "-" + URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, formattedFileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(new Date(System.currentTimeMillis() + presignedUrlExpirationMillis));
        return new FileUploadResponse(formattedFileName, s3Client.generatePresignedUrl(request).toString());
    }
}
