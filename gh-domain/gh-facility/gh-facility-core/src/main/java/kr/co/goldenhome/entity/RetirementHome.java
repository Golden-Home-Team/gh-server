package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "retirement_homes")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RetirementHome { // 양로원

    @Id
    private Long id;
    private String districtName;
    private String name;
    private String director;
    private Integer capacity;
    private Integer currentTotal;
    private Integer currentMale;
    private Integer currentFemale;
    private Integer staffTotal;
    private Integer staffMale;
    private Integer staffFemale;
    private String address;
    private String phoneNumber;
    private String establishmentDate;
    private String operatingBody;

    @Builder
    private RetirementHome(Long id, String districtName, String name, String director, Integer capacity, Integer currentTotal, Integer currentMale, Integer currentFemale, Integer staffTotal, Integer staffMale, Integer staffFemale, String address, String phoneNumber, String establishmentDate, String operatingBody) {
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
        this.establishmentDate = establishmentDate;
        this.operatingBody = operatingBody;
    }

    public static RetirementHome from(Facility facility) {

        return RetirementHome.builder()
                .id(facility.getId())
                .districtName(facility.getDistrictName())
                .name(facility.getName())
                .director(facility.getDirector())
                .capacity(Facility.parseSafeInteger(facility.getCapacity()))
                .currentTotal(Facility.parseSafeInteger(facility.getCurrentTotal()))
                .currentMale(Facility.parseSafeInteger(facility.getCurrentMale()))
                .currentFemale(Facility.parseSafeInteger(facility.getCurrentFemale()))
                .staffTotal(Facility.parseSafeInteger(facility.getStaffTotal()))
                .staffMale(Facility.parseSafeInteger(facility.getStaffMale()))
                .staffFemale(Facility.parseSafeInteger(facility.getStaffFemale()))
                .address(facility.getAddress())
                .phoneNumber(facility.getPhoneNumber())
//                .establishmentDate(facility.getEstablishmentDate())
//                .operatingBody(facility.getOperatingBody())
                .build();
    }


}
