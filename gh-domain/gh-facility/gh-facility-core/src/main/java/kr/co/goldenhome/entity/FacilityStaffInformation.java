package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.dto.SqliteStaffInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "facility_staff_information")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class FacilityStaffInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String institutionSymbol;
    private Integer directorCount;
    private Integer headOfOfficeCount;
    private Integer socialWorkerCount;
    private Integer residentDoctorCount;
    private Integer visitingDoctorCount;
    private Integer nurseCount;
    private Integer assistantNurseCount;
    private Integer dentalHygienistCount;
    private Integer physicalTherapistCount;
    private Integer occupationalTherapistCount;
    @Column(name = "caregiver_level1_count")
    private Integer caregiverLevel1Count;
    @Column(name = "caregiver_level2_count")
    private Integer caregiverLevel2Count;
    private Integer caregiverDeferredCount;
    private Integer officeWorkerCount;
    private Integer dietitianCount;
    private Integer cookCount;
    private Integer hygieneWorkerCount;
    private Integer maintenanceWorkerCount;
    private Integer assistantWorkerCount;
    private Integer otherWorkerCount;
    private Integer staffTotal;

    @Builder
    private FacilityStaffInformation(Long id, String institutionSymbol, Integer directorCount, Integer headOfOfficeCount, Integer socialWorkerCount, Integer residentDoctorCount, Integer visitingDoctorCount, Integer nurseCount, Integer assistantNurseCount, Integer dentalHygienistCount, Integer physicalTherapistCount, Integer occupationalTherapistCount, Integer caregiverLevel1Count, Integer caregiverLevel2Count, Integer caregiverDeferredCount, Integer officeWorkerCount, Integer dietitianCount, Integer cookCount, Integer hygieneWorkerCount, Integer maintenanceWorkerCount, Integer assistantWorkerCount, Integer otherWorkerCount, Integer staffTotal) {
        this.id = id;
        this.institutionSymbol = institutionSymbol;
        this.directorCount = directorCount;
        this.headOfOfficeCount = headOfOfficeCount;
        this.socialWorkerCount = socialWorkerCount;
        this.residentDoctorCount = residentDoctorCount;
        this.visitingDoctorCount = visitingDoctorCount;
        this.nurseCount = nurseCount;
        this.assistantNurseCount = assistantNurseCount;
        this.dentalHygienistCount = dentalHygienistCount;
        this.physicalTherapistCount = physicalTherapistCount;
        this.occupationalTherapistCount = occupationalTherapistCount;
        this.caregiverLevel1Count = caregiverLevel1Count;
        this.caregiverLevel2Count = caregiverLevel2Count;
        this.caregiverDeferredCount = caregiverDeferredCount;
        this.officeWorkerCount = officeWorkerCount;
        this.dietitianCount = dietitianCount;
        this.cookCount = cookCount;
        this.hygieneWorkerCount = hygieneWorkerCount;
        this.maintenanceWorkerCount = maintenanceWorkerCount;
        this.assistantWorkerCount = assistantWorkerCount;
        this.otherWorkerCount = otherWorkerCount;
        this.staffTotal = staffTotal;
    }

    public static FacilityStaffInformation from(SqliteStaffInfo sqliteStaffInfo) {
        return FacilityStaffInformation
                .builder()
                .id(sqliteStaffInfo.getIndex())
                .institutionSymbol(sqliteStaffInfo.getFacilityId())
                .directorCount(sqliteStaffInfo.getDirectorCount())
                .headOfOfficeCount(sqliteStaffInfo.getHeadOfOfficeCount())
                .socialWorkerCount(sqliteStaffInfo.getSocialWorkerCount())
                .residentDoctorCount(sqliteStaffInfo.getResidentDoctorCount())
                .visitingDoctorCount(sqliteStaffInfo.getVisitingDoctorCount())
                .nurseCount(sqliteStaffInfo.getNurseCount())
                .assistantNurseCount(sqliteStaffInfo.getAssistantNurseCount())
                .dentalHygienistCount(sqliteStaffInfo.getDentalHygienistCount())
                .physicalTherapistCount(sqliteStaffInfo.getPhysicalTherapistCount())
                .occupationalTherapistCount(sqliteStaffInfo.getOccupationalTherapistCount())
                .caregiverLevel1Count(sqliteStaffInfo.getCaregiverLevel1Count())
                .caregiverLevel2Count(sqliteStaffInfo.getCaregiverLevel2Count())
                .caregiverDeferredCount(sqliteStaffInfo.getCaregiverDeferredCount())
                .officeWorkerCount(sqliteStaffInfo.getOfficeWorkerCount())
                .dietitianCount(sqliteStaffInfo.getDietitianCount())
                .cookCount(sqliteStaffInfo.getCookCount())
                .hygieneWorkerCount(sqliteStaffInfo.getHygieneWorkerCount())
                .maintenanceWorkerCount(sqliteStaffInfo.getMaintenanceWorkerCount())
                .assistantWorkerCount(sqliteStaffInfo.getAssistantWorkerCount())
                .otherWorkerCount(sqliteStaffInfo.getOtherWorkerCount())
                .staffTotal(sqliteStaffInfo.getStaffTotal())
                .build();
    }

}
