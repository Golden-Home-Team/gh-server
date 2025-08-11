package kr.co.goldenhome.dto;


import java.util.List;

public record FacilityDetailServiceResponse(
        Long id,
        String institutionSymbol,
        String name,
        String facilityType,
        String address,
        String phoneNumber,
        Integer establishmentDate,
        String grade,
        Integer capacity,
        Integer currentTotal,
        Integer currentMale,
        Integer currentFemale,
        FacilityInfoInnerResponse facilityInfoInnerResponse,
        FacilityStaffInnerResponse facilityStaffInnerResponse,
        List<FacilityPhotoResponse> photoResponses,
        List<FacilityProgramResponse> facilityProgramResponses
) {

    public static FacilityDetailServiceResponse of(FacilityCombinedDto facilityCombinedDto, List<FacilityPhotoResponse> facilityPhotoResponses, List<FacilityProgramResponse> facilityProgramResponses) {
        return new FacilityDetailServiceResponse(
                facilityCombinedDto.id(),
                facilityCombinedDto.institutionSymbol(),
                facilityCombinedDto.name(),
                facilityCombinedDto.facilityType(),
                facilityCombinedDto.address(),
                facilityCombinedDto.phoneNumber(),
                facilityCombinedDto.establishmentDate(),
                facilityCombinedDto.grade(),
                facilityCombinedDto.capacity(),
                facilityCombinedDto.currentTotal(),
                facilityCombinedDto.currentMale(),
                facilityCombinedDto.currentFemale(),
                new FacilityInfoInnerResponse(
                        facilityCombinedDto.facilityDetailId(),
                        facilityCombinedDto.singleRoomCount(),
                        facilityCombinedDto.doubleRoomCount(),
                        facilityCombinedDto.tripleRoomCount(),
                        facilityCombinedDto.quadRoomCount(),
                        facilityCombinedDto.officeCount(),
                        facilityCombinedDto.medicalNurseRoomCount(),
                        facilityCombinedDto.dailyLivingTrainingRoomCount(),
                        facilityCombinedDto.programRoomCount(),
                        facilityCombinedDto.kitchenDiningRoomCount(),
                        facilityCombinedDto.bathroomCount(),
                        facilityCombinedDto.washBathRoomCount(),
                        facilityCombinedDto.laundryRoomCount()
                ),
                new FacilityStaffInnerResponse(
                        facilityCombinedDto.facilityStaffInformationId(),
                        facilityCombinedDto.directorCount(),
                        facilityCombinedDto.headOfOfficeCount(),
                        facilityCombinedDto.socialWorkerCount(),
                        facilityCombinedDto.residentDoctorCount(),
                        facilityCombinedDto.visitingDoctorCount(),
                        facilityCombinedDto.nurseCount(),
                        facilityCombinedDto.assistantNurseCount(),
                        facilityCombinedDto.dentalHygienistCount(),
                        facilityCombinedDto.physicalTherapistCount(),
                        facilityCombinedDto.occupationalTherapistCount(),
                        facilityCombinedDto.caregiverLevel1Count(),
                        facilityCombinedDto.caregiverLevel2Count(),
                        facilityCombinedDto.caregiverDeferredCount(),
                        facilityCombinedDto.officeWorkerCount(),
                        facilityCombinedDto.dietitianCount(),
                        facilityCombinedDto.cookCount(),
                        facilityCombinedDto.hygieneWorkerCount(),
                        facilityCombinedDto.maintenanceWorkerCount(),
                        facilityCombinedDto.assistantWorkerCount(),
                        facilityCombinedDto.otherWorkerCount()
                ),
                facilityPhotoResponses,
                facilityProgramResponses
        );
    }


}
