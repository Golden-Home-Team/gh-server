package kr.co.goldenhome.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

@Getter
@Document(indexName = "facilities")
@Setting(settingPath = "/elasticsearch/facility-settings.json")
public class FacilityDocument {

    @Id
    private String id;
    @Field(type = FieldType.Keyword)
    private String institutionSymbol;
    @Field(type = FieldType.Text, analyzer = "facilities_analyzer")
    private String facilityType;
    @Field(type = FieldType.Text, analyzer = "facilities_analyzer")
    private String name;
    @Field(type = FieldType.Text, analyzer = "facilities_analyzer")
    private String address;
    @Field(type = FieldType.Integer)
    private Integer establishmentYear;
    @Setter
    @Field(type = FieldType.Keyword)
    private String grade;
    @Field(type = FieldType.Integer)
    private Integer capacity;
    @Field(type = FieldType.Integer)
    private Integer currentTotal;
    @Field(type = FieldType.Integer)
    private Integer viewCount;
    @Field(type = FieldType.Integer)
    private Integer reviewCount;
    @Field(type = FieldType.Integer)
    private Integer likeCount;
    @Field(type = FieldType.Integer)
    private Integer consultationCount;
    @Setter
    @Field(type = FieldType.Keyword)
    private List<String> imageUrls;
    @Setter
    @Field
    private GeoPoint location;

    @Builder
    private FacilityDocument(String id, String institutionSymbol, String facilityType, String name, String address, Integer establishmentYear, String grade, Integer capacity, Integer currentTotal, Integer viewCount, Integer reviewCount, Integer likeCount, Integer consultationCount, GeoPoint location) {
        this.id = id;
        this.institutionSymbol = institutionSymbol;
        this.facilityType = facilityType;
        this.name = name;
        this.address = address;
        this.establishmentYear = establishmentYear;
        this.grade = grade;
        this.capacity = capacity;
        this.currentTotal = currentTotal;
        this.viewCount = viewCount;
        this.reviewCount = reviewCount;
        this.likeCount = likeCount;
        this.consultationCount = consultationCount;
        this.location = location;
    }

    public static FacilityDocument from(Facility facility) {
        return FacilityDocument.builder()
                .id(facility.getId().toString())
                .institutionSymbol(facility.getInstitutionSymbol())
                .facilityType(facility.getFacilityType())
                .name(facility.getName())
                .address(facility.getAddress())
                .establishmentYear(facility.getEstablishmentDate())
                .capacity(facility.getCapacity())
                .currentTotal(facility.getCurrentTotal())
                .viewCount(0)
                .reviewCount(0)
                .likeCount(0)
                .consultationCount(0)
                .build();
    }

}
