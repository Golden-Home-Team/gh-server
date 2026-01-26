package kr.co.goldenhome.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.goldenhome.dto.FacilityCombinedDto;
import kr.co.goldenhome.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FacilityRepositoryImpl implements FacilityRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final FacilityJpaRepository facilityJpaRepository;

    @Override
    public FacilityCombinedDto read(Long facilityId) {
        QFacility facility = QFacility.facility;
        QFacilityDetail facilityDetail = QFacilityDetail.facilityDetail;
        QFacilityStaffInformation facilityStaffInformation = QFacilityStaffInformation.facilityStaffInformation;
        QFacilityGrade facilityGrade = QFacilityGrade.facilityGrade;
        return jpaQueryFactory
                .select(Projections.constructor(
                        FacilityCombinedDto.class,
                        facility.id,
                        facility.institutionSymbol,
                        facility.name,
                        facility.facilityType,
                        facility.address,
                        facility.phoneNumber,
                        facility.establishmentDate,
                        facilityGrade.grade,
                        facility.capacity,
                        facility.currentTotal,
                        facility.currentMale,
                        facility.currentFemale,
                        facility.latitude,
                        facility.longitude,
                        facilityDetail.id,
                        facilityDetail.singleRoomCount,
                        facilityDetail.doubleRoomCount,
                        facilityDetail.tripleRoomCount,
                        facilityDetail.quadRoomCount,
                        facilityDetail.officeCount,
                        facilityDetail.medicalNurseRoomCount,
                        facilityDetail.dailyLivingTrainingRoomCount,
                        facilityDetail.programRoomCount,
                        facilityDetail.kitchenDiningRoomCount,
                        facilityDetail.bathroomCount,
                        facilityDetail.washBathRoomCount,
                        facilityDetail.laundryRoomCount,
                        facilityStaffInformation.id,
                        facilityStaffInformation.directorCount,
                        facilityStaffInformation.headOfOfficeCount,
                        facilityStaffInformation.socialWorkerCount,
                        facilityStaffInformation.residentDoctorCount,
                        facilityStaffInformation.visitingDoctorCount,
                        facilityStaffInformation.nurseCount,
                        facilityStaffInformation.assistantNurseCount,
                        facilityStaffInformation.dentalHygienistCount,
                        facilityStaffInformation.physicalTherapistCount,
                        facilityStaffInformation.occupationalTherapistCount,
                        facilityStaffInformation.caregiverLevel1Count,
                        facilityStaffInformation.caregiverLevel2Count,
                        facilityStaffInformation.caregiverDeferredCount,
                        facilityStaffInformation.officeWorkerCount,
                        facilityStaffInformation.dietitianCount,
                        facilityStaffInformation.cookCount,
                        facilityStaffInformation.hygieneWorkerCount,
                        facilityStaffInformation.maintenanceWorkerCount,
                        facilityStaffInformation.assistantWorkerCount,
                        facilityStaffInformation.otherWorkerCount
                ))
                .from(facility)
                .where(facility.id.eq(facilityId))
                .leftJoin(facilityDetail).on(facility.institutionSymbol.eq(facilityDetail.institutionSymbol))
                .leftJoin(facilityStaffInformation).on(facility.institutionSymbol.eq(facilityStaffInformation.institutionSymbol))
                .leftJoin(facilityGrade).on(facility.institutionSymbol.eq(facilityGrade.institutionSymbol))
                .fetchFirst();

    }

    @Override
    public List<Facility> findByIdIn(List<Long> facilityIds) {
        return facilityJpaRepository.findByIdIn(facilityIds);
    }

    @Override
    public List<Facility> searchByFullTextFallback(String keyword, int page, int size) {
        return facilityJpaRepository.searchByFullTextFallback(keyword, PageRequest.of(page-1, size));
    }

    @Override
    public List<Facility> searchByLikeFallback(String keyword, int page, int size) {
        return facilityJpaRepository.searchByLikeFallback(keyword, PageRequest.of(page-1, size));
    }
}
