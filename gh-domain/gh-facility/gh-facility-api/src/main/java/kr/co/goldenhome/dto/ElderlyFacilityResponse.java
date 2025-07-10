package kr.co.goldenhome.dto;

import kr.co.goldenhome.entity.ElderlyFacility;

public record ElderlyFacilityResponse(
         Long id,
         String districtName,
         String name,
         String director,
         Integer capacity,
         Integer currentTotal,
         Integer currentMale,
         Integer currentFemale,
         Integer staffTotal,
         Integer staffMale,
         Integer staffFemale,
         String address,
         String phoneNumber,
         String facilityType
) {
    public static ElderlyFacilityResponse from(ElderlyFacility facility) {
        return new ElderlyFacilityResponse(
                facility.getId(),
                facility.getDistrictName(),
                facility.getName(),
                facility.getDirector(),
                facility.getCapacity(),
                facility.getCurrentTotal(),
                facility.getCurrentMale(),
                facility.getCurrentFemale(),
                facility.getStaffTotal(),
                facility.getStaffMale(),
                facility.getStaffFemale(),
                facility.getAddress(),
                facility.getPhoneNumber(),
                facility.getFacilityType()
        );
    }
}
