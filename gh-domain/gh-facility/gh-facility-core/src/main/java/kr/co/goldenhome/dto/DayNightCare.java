package kr.co.goldenhome.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Table(name = "day_night_cares")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DayNightCare {

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
    private DayNightCare(Long id, String districtName, String name, String director, Integer capacity, Integer currentTotal, Integer currentMale, Integer currentFemale, Integer staffTotal, Integer staffMale, Integer staffFemale, String address, String phoneNumber, String establishmentDate, String operatingBody) {
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

    public static DayNightCare from(Service facility) {
        return DayNightCare.builder()
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
