package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.enums.DailyShotType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(
        name = "daily_shot_images",
        indexes = {
                @Index(name = "idx_daily_shot_images_daily_shot_id", columnList = "daily_shot_id")
        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyShotImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long dailyShotId;
    private String content;
    @Enumerated(EnumType.STRING)
    private DailyShotType dailyShotType;
    private String imageUrl;
    private LocalDateTime createdAt;

    @Builder
    private DailyShotImage(Long id, Long dailyShotId, String content, DailyShotType dailyShotType, String imageUrl, LocalDateTime createdAt) {
        this.id = id;
        this.dailyShotId = dailyShotId;
        this.content = content;
        this.dailyShotType = dailyShotType;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public static DailyShotImage create(Long dailyShotId, DailyShotType dailyShotType, String imageUrl) {
        return DailyShotImage.builder()
                .dailyShotId(dailyShotId)
                .dailyShotType(dailyShotType)
                .imageUrl(imageUrl)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
