package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.dto.SqliteDetailFacility;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "facility_details")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class FacilityDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String institutionSymbol;
    private String singleRoomCount;
    private String doubleRoomCount;
    private String tripleRoomCount;
    private String quadRoomCount;
    private String specialBedroomCount;
    private String officeCount;
    private String medicalNurseRoomCount;
    private String dailyLivingTrainingRoomCount;
    private String programRoomCount;
    private String kitchenDiningRoomCount;
    private String bathroomCount;
    private String washBathRoomCount;
    private String laundryRoomCount;

    @Builder
    private FacilityDetail(Long id, String institutionSymbol, String singleRoomCount, String doubleRoomCount, String tripleRoomCount, String quadRoomCount, String specialBedroomCount, String officeCount, String medicalNurseRoomCount, String dailyLivingTrainingRoomCount, String programRoomCount, String kitchenDiningRoomCount, String bathroomCount, String washBathRoomCount, String laundryRoomCount) {
        this.id = id;
        this.institutionSymbol = institutionSymbol;
        this.singleRoomCount = singleRoomCount;
        this.doubleRoomCount = doubleRoomCount;
        this.tripleRoomCount = tripleRoomCount;
        this.quadRoomCount = quadRoomCount;
        this.specialBedroomCount = specialBedroomCount;
        this.officeCount = officeCount;
        this.medicalNurseRoomCount = medicalNurseRoomCount;
        this.dailyLivingTrainingRoomCount = dailyLivingTrainingRoomCount;
        this.programRoomCount = programRoomCount;
        this.kitchenDiningRoomCount = kitchenDiningRoomCount;
        this.bathroomCount = bathroomCount;
        this.washBathRoomCount = washBathRoomCount;
        this.laundryRoomCount = laundryRoomCount;
    }

    public static FacilityDetail from(SqliteDetailFacility sqliteDetailFacility) {
        return FacilityDetail.builder()
                .id(sqliteDetailFacility.getIndex())
                .institutionSymbol(sqliteDetailFacility.getFacilityId())
                .singleRoomCount(sqliteDetailFacility.getSingleRoomCount())
                .doubleRoomCount(sqliteDetailFacility.getDoubleRoomCount())
                .tripleRoomCount(sqliteDetailFacility.getTripleRoomCount())
                .quadRoomCount(sqliteDetailFacility.getQuadRoomCount())
                .specialBedroomCount(sqliteDetailFacility.getSpecialBedroomCount())
                .officeCount(sqliteDetailFacility.getOfficeCount())
                .medicalNurseRoomCount(sqliteDetailFacility.getMedicalNurseRoomCount())
                .dailyLivingTrainingRoomCount(sqliteDetailFacility.getDailyLivingTrainingRoomCount())
                .programRoomCount(sqliteDetailFacility.getProgramRoomCount())
                .kitchenDiningRoomCount(sqliteDetailFacility.getKitchenDiningRoomCount())
                .bathroomCount(sqliteDetailFacility.getBathroomCount())
                .washBathRoomCount(sqliteDetailFacility.getWashBathRoomCount())
                .laundryRoomCount(sqliteDetailFacility.getLaundryRoomCount())
                .build();
    }
}
