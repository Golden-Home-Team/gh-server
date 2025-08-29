package kr.co.goldenhome;

//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.AmazonS3Exception;
//import com.amazonaws.services.s3.model.DeleteObjectRequest;
import io.awspring.cloud.s3.S3Exception;
import kr.co.goldenhome.entity.ProfileImage;
import kr.co.goldenhome.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;


@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileImageApiImpl implements ProfileImageApi {

    private final ProfileImageRepository profileImageRepository;
    @Value("${aws.s3.base-url}")
    private String awsBaseUrl;
    @Value("${aws.s3.bucket}")
    private String bucket;
    private final S3Client s3Client;

    @Override
    public ProfileImageApiResponse getByUserId(Long userId) {
        return profileImageRepository.findByUserId(userId)
                .map(p -> new ProfileImageApiResponse(p.getId(), p.getFormattedName(), p.getImageUrl()))
                .orElse(null);
    }

    /**
     * 프로필 이미지는 저장 시 기존 이미지를 물리적으로 삭제한다.
     * 유저는 인당 1개의 프로필 이미지를 갖는다.
     * 새 이미지는 프론트엔드에서 Presigned URL 로 S3에 이미 업로드된 상태라고 가정한다.
     */

    @Override
    @Transactional
    public void save(String formattedImageName, Long userId) {
        String newImageUrl = awsBaseUrl + formattedImageName;
        ProfileImage oldProfileImage = profileImageRepository.findByUserId(userId).orElse(null);
        String oldFormattedName = (oldProfileImage != null) ? oldProfileImage.getFormattedName() : null;

        try {
            if (oldProfileImage != null) {
                profileImageRepository.deleteById(userId);
            }
            profileImageRepository.save(ProfileImage.create(userId, formattedImageName, newImageUrl));

            if (oldFormattedName != null) {
                try {
                    DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                            .bucket(bucket)
                            .key(oldFormattedName)
                            .build();
                    s3Client.deleteObject(deleteRequest);
                    log.info("기존 프로필 이미지 S3 삭제 성공: {}", oldFormattedName);
                } catch (S3Exception e) {
                    log.error("기존 프로필 이미지 S3 삭제 실패 (고아 이미지 가능성): {}", oldFormattedName, e);
                    throw e;
                }
            }
        } catch (RuntimeException dbException) {
            log.error("프로필 이미지 DB 업데이트 실패. S3에 업로드된 새 이미지 롤백 시도: {}", formattedImageName, dbException);
            try {
                DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(formattedImageName)
                        .build();
                s3Client.deleteObject(deleteRequest);
                log.info("DB 업데이트 실패로 인해 S3 새 이미지 롤백 성공: {}", formattedImageName);
            } catch (S3Exception s3DeleteException) {
                log.error("DB 트랜잭션 롤백 중 S3 새 프로필 이미지 삭제 실패 (고아 이미지 가능성): {}", formattedImageName, s3DeleteException);
            }
            throw dbException;
        }

    }


}
