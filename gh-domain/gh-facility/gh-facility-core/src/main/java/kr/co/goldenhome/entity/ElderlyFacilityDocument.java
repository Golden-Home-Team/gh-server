package kr.co.goldenhome.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Document(indexName = "elderly_facilities")
@Setting(settingPath = "/elasticsearch/facility-settings.json")
public class ElderlyFacilityDocument {

    @Id
    private String id;
    @Field(type = FieldType.Keyword)
    private String districtName;
    @Field(type = FieldType.Text, analyzer = "elderly_facilities_analyzer")
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
    @Field(type = FieldType.Text, analyzer = "elderly_facilities_analyzer")
    private String address;
    @Field(type = FieldType.Keyword)
    private String phoneNumber;
    @Field(type = FieldType.Integer)
    private Integer establishmentYear;
    @Field(type = FieldType.Keyword)
    private String operatingBody;
    @Field(type = FieldType.Keyword)
    private String facilityType;
    @Field(type = FieldType.Keyword)
    private String grade;
    @Field(type = FieldType.Integer)
    private Integer price;

    @Builder
    private ElderlyFacilityDocument(String id, String districtName, String name, String director, Integer capacity, Integer currentTotal, Integer currentMale, Integer currentFemale, Integer staffTotal, Integer staffMale, Integer staffFemale, String address, String phoneNumber, Integer establishmentYear, String operatingBody, String facilityType, String grade, Integer price) {
        this.id = id;
        this.districtName = districtName;
        this.name = name;
        this.director = director;
        this.capacity = capacity;
        this.currentTotal = currentTotal;
        this.currentMale = currentMale;
        this.currentFemale = currentFemale;
        this.staffTotal = staffTotal;
        this.staffMale = staffMale;
        this.staffFemale = staffFemale;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.establishmentYear = establishmentYear;
        this.operatingBody = operatingBody;
        this.facilityType = facilityType;
        this.grade = grade;
        this.price = price;
    }

    public static ElderlyFacilityDocument from(ElderlyFacility facility) {
        return ElderlyFacilityDocument.builder()
                .id(facility.getId().toString())
                .districtName(facility.getDistrictName())
                .name(facility.getName())
                .director(facility.getDirector())
                .capacity(facility.getCapacity())
                .currentTotal(facility.getCurrentTotal())
                .currentMale(facility.getCurrentMale())
                .currentFemale(facility.getCurrentFemale())
                .staffTotal(facility.getStaffTotal())
                .staffMale(facility.getStaffMale())
                .staffFemale(facility.getStaffFemale())
                .address(facility.getAddress())
                .phoneNumber(facility.getPhoneNumber())
                .operatingBody(facility.getOperatingBody())
                .facilityType(facility.getFacilityType())
                .establishmentYear(2024)
                .grade("A")
                .price(15000)
                .build();
    }
}
