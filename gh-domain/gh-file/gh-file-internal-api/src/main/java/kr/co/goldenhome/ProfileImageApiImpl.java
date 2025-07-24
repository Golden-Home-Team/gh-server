package kr.co.goldenhome;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import kr.co.goldenhome.entity.ProfileImage;
import kr.co.goldenhome.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileImageApiImpl implements ProfileImageApi {

    private final ProfileImageRepository profileImageRepository;
    @Value("${aws.s3.base-url}")
    private String awsBaseUrl;
    @Value("${aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

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
                    amazonS3.deleteObject(new DeleteObjectRequest(bucket, oldFormattedName));
                    log.info("기존 프로필 이미지 S3 삭제 성공: {}", oldFormattedName);
                } catch (AmazonS3Exception e) {
                    // 필요한 경우 실패한 삭제 요청을 큐에 넣어 재시도
                    log.error("기존 프로필 이미지 S3 삭제 실패 (고아 이미지 가능성): {}", oldFormattedName, e);
                    throw e;
                }
            }
        } catch (RuntimeException dbException) {
            log.error("프로필 이미지 DB 업데이트 실패. S3에 업로드된 새 이미지 롤백 시도: {}", formattedImageName, dbException);
            try {
                amazonS3.deleteObject(new DeleteObjectRequest(bucket, formattedImageName));
                log.info("DB 업데이트 실패로 인해 S3 새 이미지 롤백 성공: {}", formattedImageName);
            } catch (AmazonS3Exception s3DeleteException) {
                log.error("DB 트랜잭션 롤백 중 S3 새 프로필 이미지 삭제 실패 (고아 이미지 가능성): {}", formattedImageName, s3DeleteException);
            }
            throw dbException;
        }

    }

}
