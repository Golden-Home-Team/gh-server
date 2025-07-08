package kr.co.goldenhome.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "siver_towns")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SiverTown {

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
    private SiverTown(Long id, String districtName, String name, String director, Integer capacity, Integer currentTotal, Integer currentMale, Integer currentFemale, Integer staffTotal, Integer staffMale, Integer staffFemale, String address, String phoneNumber, String establishmentDate, String operatingBody) {
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

    public static SiverTown from(Facility facility) {
        return SiverTown.builder()
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
