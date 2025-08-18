package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.enums.DailyDietType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(
        name = "daily_diet_images",
        indexes = {
                @Index(name = "idx_daily_diet_images_daily_diet_id", columnList = "daily_diet_id")
        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyDietImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long dailyDietId;
    @Enumerated(EnumType.STRING)
    private DailyDietType dailyDietType;
    private String formattedName;
    private String imageUrl;
    private LocalDateTime createdAt;

    @Builder
    private DailyDietImage(Long id, Long dailyDietId, DailyDietType dailyDietType, String formattedName, String imageUrl, LocalDateTime createdAt) {
        this.id = id;
        this.dailyDietId = dailyDietId;
        this.dailyDietType = dailyDietType;
        this.formattedName = formattedName;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public static DailyDietImage create(Long dailyDietId, DailyDietType dailyDietType, String formattedName, String imageUrl) {
        return DailyDietImage.builder()
                .dailyDietId(dailyDietId)
                .formattedName(formattedName)
                .dailyDietType(dailyDietType)
                .imageUrl(imageUrl)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
