package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
        name = "review_images",
        indexes = {
                @Index(name = "idx_review_images_review_id", columnList = "review_id")
        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reviewId;
    private String formattedName;
    private String imageUrl;

    @Builder
    private ReviewImage(Long id, Long reviewId, String formattedName, String imageUrl) {
        this.id = id;
        this.reviewId = reviewId;
        this.formattedName = formattedName;
        this.imageUrl = imageUrl;
    }

    public static ReviewImage create(Long reviewId,  String formattedName, String imageUrl) {
        return ReviewImage.builder()
                .reviewId(reviewId)
                .formattedName(formattedName)
                .imageUrl(imageUrl)
                .build();
    }
}
