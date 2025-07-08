package kr.co.goldenhome.entity;


import jakarta.persistence.*;
import lombok.*;

@Table(name = "facilities")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type; // 1. 양로시설
    private String name; // 시설명
    private String districtName; // 시?군?구
    private String director; // 시설장
    private String capacity; // 정원
    private String currentTotal; // 입소 현황 - 계
    private String currentMale;  // 입소 현황 - 남
    private String currentFemale; // 입소 현황 - 여
    private String staffTotal;   // 종사자 수 - 계
    private String staffMale;    // 종사자 수 - 남
    private String staffFemale;  // 종사자 수 - 여
    private String address; // 소재지
    private String phoneNumber; // 전화번호(FAX 번호)
    private String establishmentDate; // 설 치 일 (날짜 형식이 아니므로 String 으로 처리)
    private String operatingBody; // 운 영 주 체


    public static Facility batch(String type, String name, String districtName, String director, String capacity, String currentTotal, String currentMale, String currentFemale, String staffTotal, String staffMale, String staffFemale, String address, String phoneNumber, String establishmentDate, String operatingBody) {
        Facility facility = new Facility();
        facility.setType(type);
        facility.setName(name);
        facility.setDistrictName(districtName);
        facility.setDirector(director);
        facility.setCapacity(capacity);
        facility.setCurrentTotal(currentTotal);
        facility.setCurrentMale(currentMale);
        facility.setCurrentFemale(currentFemale);
        facility.setStaffTotal(staffTotal);
        facility.setStaffMale(staffMale);
        facility.setStaffFemale(staffFemale);
        facility.setAddress(address);
        facility.setPhoneNumber(phoneNumber);
        facility.setOperatingBody(operatingBody);
        return facility;
    }

    public static Integer parseSafeInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null; // 값이 없으면 null 반환
        }
        try {
            return Integer.valueOf(value.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
