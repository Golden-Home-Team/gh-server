package kr.co.goldenhome.entity;


import jakarta.persistence.*;
import kr.co.goldenhome.dto.SqliteFacility;
import lombok.*;

import java.io.Serializable;

@Table(name = "facilities")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String institutionSymbol;
    private String facilityType;
    private String name;
    private String address;
    private String grade; // 지우기 FacilityRepositoryImpl
    private String phoneNumber;
    private String email;
    private String homepage;
    private Integer establishmentDate; 
    private String districtName;
    private Integer capacity;
    private Integer currentMale;
    private Integer currentFemale;
    private Integer currentTotal;
    private String staffTotal;

    @Builder
    private Facility(Long id, String institutionSymbol, String facilityType, String name, String grade, String address, String phoneNumber, String email, String homepage, Integer establishmentDate, String districtName, Integer capacity, Integer currentMale, Integer currentFemale, Integer currentTotal, String staffTotal) {
        this.id = id;
        this.institutionSymbol = institutionSymbol;
        this.facilityType = facilityType;
        this.name = name;
        this.address = address;
        this.grade = grade;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.homepage = homepage;
        this.establishmentDate = establishmentDate;
        this.districtName = districtName;
        this.capacity = capacity;
        this.currentMale = currentMale;
        this.currentFemale = currentFemale;
        this.currentTotal = currentTotal;
        this.staffTotal = staffTotal;
    }

    public static Facility from(SqliteFacility sqliteFacility) {
        String facilityCapacity = sqliteFacility.getCapacity();
        String facilityMale = sqliteFacility.getCurrentMale();
        String facilityFemale = sqliteFacility.getCurrentFemale();
        String facilityTotal = sqliteFacility.getCurrentTotal();
        String facilityStaffTotal = sqliteFacility.getStaffTotal();
        return Facility.builder()
                .id(sqliteFacility.getIndex())
                .institutionSymbol(sqliteFacility.getId())
                .facilityType(sqliteFacility.getFacilityType())
                .name(sqliteFacility.getName())
                .address(sqliteFacility.getAddress())
                .phoneNumber(sqliteFacility.getPhoneNumber())
                .email(sqliteFacility.getEmail())
                .homepage(sqliteFacility.getHomepage())
                .establishmentDate(sqliteFacility.getEstablishmentDate())
                .districtName(sqliteFacility.getDistrictName())
                .capacity(facilityCapacity == null ? null : Integer.valueOf(facilityCapacity))
                .currentMale(facilityMale == null ? null : Integer.valueOf(facilityMale))
                .currentFemale(facilityFemale == null ? null : Integer.valueOf(facilityFemale))
                .currentTotal(facilityTotal == null ? null : Integer.valueOf(facilityTotal))
                .staffTotal(facilityStaffTotal)
                .build();
    }

    public Facility calculateTotal() {
        if (currentMale != null && currentFemale != null) currentTotal = (currentMale+currentFemale);
        return this;
    }
}
