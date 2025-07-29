package kr.co.goldenhome.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqliteStaffInfo {
    private Long index;
    private String facilityId;
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
    private Integer caregiverLevel1Count;
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
}
