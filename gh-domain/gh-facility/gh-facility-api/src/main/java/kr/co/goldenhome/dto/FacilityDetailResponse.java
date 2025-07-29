package kr.co.goldenhome.dto;


import kr.co.goldenhome.entity.FacilityPhoto;
import kr.co.goldenhome.entity.FacilityProgram;

import java.util.List;

public record FacilityDetailResponse(
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
        FacilityInfoResponse facilityInfoResponse,
        FacilityStaffResponse facilityStaffResponse,
        List<FacilityPhotoResponse> photoResponses,
        List<FacilityProgramResponse> facilityProgramResponses
) {

    public static FacilityDetailResponse of(FacilityCombinedDto facilityCombinedDto, List<FacilityPhotoResponse> facilityPhotoResponses, List<FacilityProgramResponse> facilityProgramResponses) {
        return new FacilityDetailResponse(
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
                new FacilityInfoResponse(
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
                new FacilityStaffResponse(
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

    public record FacilityStaffResponse(
            Long facilityStaffInformationId,
            Integer directorCount,
            Integer headOfOfficeCount,
            Integer socialWorkerCount,
            Integer residentDoctorCount,
            Integer visitingDoctorCount,
            Integer nurseCount,
            Integer assistantNurseCount,
            Integer dentalHygienistCount,
            Integer physicalTherapistCount,
            Integer occupationalTherapistCount,
            Integer caregiverLevel1Count,
            Integer caregiverLevel2Count,
            Integer caregiverDeferredCount,
            Integer officeWorkerCount,
            Integer dietitianCount,
            Integer cookCount,
            Integer hygieneWorkerCount,
            Integer maintenanceWorkerCount,
            Integer assistantWorkerCount,
            Integer otherWorkerCount
    ) {
    }

    public record FacilityInfoResponse(
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
}
