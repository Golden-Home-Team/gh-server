package kr.co.goldenhome.dto;

public record FacilityInfoInnerResponse(
        Long facilityDetailId,
        String singleRoomCount,
        String doubleRoomCount,
        String tripleRoomCount,
        String quadRoomCount,
        String officeCount,
        String medicalNurseRoomCount,
        String dailyLivingTrainingRoomCount,
        String programRoomCount,
        String kitchenDiningRoomCount,
        String bathroomCount,
        String washBathRoomCount,
        String laundryRoomCount
) {
}
