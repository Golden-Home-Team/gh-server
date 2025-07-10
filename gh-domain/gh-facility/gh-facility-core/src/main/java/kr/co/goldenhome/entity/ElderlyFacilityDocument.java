package kr.co.goldenhome.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "elderly_facilities")
@Setting(settingPath = "/elasticsearch/facility-settings.json")
public class ElderlyFacilityDocument {

    @Id
    private Long id;
    @Field(type = FieldType.Keyword)
    private String districtName;
    @Field(type = FieldType.Text, analyzer = "nori")
    private String name;
    @Field(type = FieldType.Keyword)
    private String director;
    @Field(type = FieldType.Integer)
    private Integer capacity;
    @Field(type = FieldType.Integer)
    private Integer currentTotal;
    @Field(type = FieldType.Integer)
    private Integer currentMale;
    @Field(type = FieldType.Integer)
    private Integer currentFemale;
    @Field(type = FieldType.Integer)
    private Integer staffTotal;
    @Field(type = FieldType.Integer)
    private Integer staffMale;
    @Field(type = FieldType.Integer)
    private Integer staffFemale;
    private String address;
    private String phoneNumber;
    private Integer establishmentDate; // todo 설립년도만 뽑아서 조회
    private String operatingBody;
    private String facilityType;
    // todo RDB 필드 추가
    private String grade;
    private Integer price;
}
