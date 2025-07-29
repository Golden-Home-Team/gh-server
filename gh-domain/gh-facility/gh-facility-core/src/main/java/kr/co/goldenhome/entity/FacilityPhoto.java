package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.dto.SqlitePhoto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "facility_photos",
        indexes = {
                @Index(name = "idx_facility_photos_institution_symbol", columnList = "institution_symbol")
        })
@Entity
@Getter
@Setter
@NoArgsConstructor
public class FacilityPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String institutionSymbol;
    private String type;
    private String name;
    private String imageUrl;
    private String description;

    @Builder
    private FacilityPhoto(Long id, String institutionSymbol, String type, String name, String imageUrl, String description) {
        this.id = id;
        this.institutionSymbol = institutionSymbol;
        this.type = type;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public static FacilityPhoto from(SqlitePhoto sqlitePhoto) {
        String cleanedDescription = null;
        if (sqlitePhoto.getDescription() != null) {
            // 모든 줄바꿈 문자를 하나의 공백으로 치환
            cleanedDescription = sqlitePhoto.getDescription().replaceAll("\\r\\n|\\r|\\n", " ");
            // 만약 여러 공백이 생길 경우를 대비하여 추가적으로 하나의 공백으로 정제
            cleanedDescription = cleanedDescription.replaceAll("\\s+", " ").trim();
        }
        return FacilityPhoto.builder()
                .id(sqlitePhoto.getIndex())
                .institutionSymbol(sqlitePhoto.getFacilityId())
                .type(sqlitePhoto.getType())
                .name(sqlitePhoto.getName())
                .imageUrl(sqlitePhoto.getImageUrl())
                .description(cleanedDescription)
                .build();
    }
}
