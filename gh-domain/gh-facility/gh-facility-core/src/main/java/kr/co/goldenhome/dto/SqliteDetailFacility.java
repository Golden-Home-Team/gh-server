package kr.co.goldenhome.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqliteDetailFacility {

    private Long index;
    private String facilityId;
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
}
