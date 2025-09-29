package kr.co.goldenhome.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqliteFacility {

    private Long index;
    private String id;
    private String facilityType;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String homepage;
    private Integer establishmentDate;
    private String districtName;
    private String capacity;
    private String currentMale;
    private String currentFemale;
    private String currentTotal;
    private String staffTotal;
    private Double latitude;
    private Double longitude;
}
