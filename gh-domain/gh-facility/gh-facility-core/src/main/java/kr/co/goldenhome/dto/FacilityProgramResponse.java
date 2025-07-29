package kr.co.goldenhome.dto;


import kr.co.goldenhome.entity.FacilityProgram;

public record FacilityProgramResponse(
        Long id,
        String institutionSymbol,
        String type,
        String name
) {
    public static FacilityProgramResponse from(FacilityProgram facilityProgram) {
        return new FacilityProgramResponse(
                facilityProgram.getId(),
                facilityProgram.getInstitutionSymbol(),
                facilityProgram.getType(),
                facilityProgram.getName()
        );
    }
}
