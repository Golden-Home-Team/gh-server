package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.dto.*;
import lombok.*;

@Table(name = "elderly_facilities")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ElderlyFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String facilityType; // 양로원, 요양원, 단기보호, 방문간호, 방문요양, 방문목욕, 주야간보호
    private String grade;
    private Integer price;
    private String size;

    @Builder
    private ElderlyFacility(Long id, String districtName, String name, String director, Integer capacity, Integer currentTotal, Integer currentMale, Integer currentFemale, Integer staffTotal, Integer staffMale, Integer staffFemale, String address, String phoneNumber, String establishmentDate, String operatingBody, String facilityType, String grade, Integer price, String size) {
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
        this.facilityType = facilityType;
        this.grade = grade;
        this.price = price;
        this.size = size;
    }

    private static String measureSize(Integer capacity) {
        if (capacity == null) return null;
        if (capacity >= 200) return "대형";
        else if (capacity >= 100) return "중형";
        else return "소형";
    }

    public static ElderlyFacility from(RetirementHome facility) {
        return ElderlyFacility.builder()
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
                .establishmentDate(facility.getEstablishmentDate())
                .operatingBody(facility.getOperatingBody())
                .facilityType("양로원")
                .size(measureSize(facility.getCapacity()))
                .build();
    }

    public static ElderlyFacility from(HomeCareFacility facility) {
        return ElderlyFacility.builder()
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
                .establishmentDate(facility.getEstablishmentDate())
                .operatingBody(facility.getOperatingBody())
                .facilityType("요양원")
                .size(measureSize(facility.getCapacity()))
                .build();
    }

    public static ElderlyFacility from(ShortTermCare facility) {
        return ElderlyFacility.builder()
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
                .establishmentDate(facility.getEstablishmentDate())
                .operatingBody(facility.getOperatingBody())
                .facilityType("단기보호")
                .size(measureSize(facility.getCapacity()))
                .build();
    }

    public static ElderlyFacility from(VisitingNursing facility) {
        return ElderlyFacility.builder()
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
                .establishmentDate(facility.getEstablishmentDate())
                .operatingBody(facility.getOperatingBody())
                .facilityType("방문간호")
                .size(measureSize(facility.getCapacity()))
                .build();
    }

    public static ElderlyFacility from(VisitingCare facility) {
        return ElderlyFacility.builder()
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
                .establishmentDate(facility.getEstablishmentDate())
                .operatingBody(facility.getOperatingBody())
                .facilityType("방문요양")
                .size(measureSize(facility.getCapacity()))
                .build();
    }

    public static ElderlyFacility from(VisitingBath facility) {
        return ElderlyFacility.builder()
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
                .establishmentDate(facility.getEstablishmentDate())
                .operatingBody(facility.getOperatingBody())
                .facilityType("방문목욕")
                .size(measureSize(facility.getCapacity()))
                .build();
    }

    public static ElderlyFacility from(DayNightCare facility) {
        return ElderlyFacility.builder()
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
                .establishmentDate(facility.getEstablishmentDate())
                .operatingBody(facility.getOperatingBody())
                .facilityType("주야간보호")
                .size(measureSize(facility.getCapacity()))
                .build();
    }

}
