package kr.co.goldenhome.dto;

public record FacilityStaffInnerResponse(
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
