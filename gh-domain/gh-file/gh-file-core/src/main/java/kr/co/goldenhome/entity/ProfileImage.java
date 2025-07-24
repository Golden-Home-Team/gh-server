package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
        name = "profile_images",
        indexes = {
                @Index(name = "idx_profile_images_user_id", columnList = "user_id")
        })
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String formattedName;
    private String imageUrl;

    @Builder
    private ProfileImage(Long id, Long userId, String formattedName, String imageUrl) {
        this.id = id;
        this.userId = userId;
        this.formattedName = formattedName;
        this.imageUrl = imageUrl;
    }

    public static ProfileImage create(Long userId, String formattedName, String imageUrl) {
        return ProfileImage.builder()
                .userId(userId)
                .formattedName(formattedName)
                .imageUrl(imageUrl)
                .build();
    }
}
