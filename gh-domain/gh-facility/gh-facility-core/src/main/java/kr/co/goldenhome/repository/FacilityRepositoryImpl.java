package kr.co.goldenhome.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.goldenhome.dto.FacilityCombinedDto;
import kr.co.goldenhome.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class FacilityRepositoryImpl implements FacilityRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final FacilityJpaRepository facilityJpaRepository;

    QFacility facility = QFacility.facility;
    QFacilityDetail facilityDetail = QFacilityDetail.facilityDetail;
    QFacilityStaffInformation facilityStaffInformation = QFacilityStaffInformation.facilityStaffInformation;
    QFacilityGrade facilityGrade = QFacilityGrade.facilityGrade;

    @Override
    public FacilityCombinedDto read(Long facilityId) {

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

    @Override
    public List<Facility> search(String name, String address, String facilityType, String grade, String sort, int withinYears, int page, int size, Double latitude, Double longitude, Double radiusKm,
                                 List<Long> priorityIds) {
        JPAQuery<Facility> query = jpaQueryFactory.selectFrom(facility);
        if (priorityIds != null && !priorityIds.isEmpty()) {
            query.where(facility.id.in(priorityIds));
            String idsString = priorityIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            query.orderBy(Expressions.stringTemplate("FIELD({0}, {1})", facility.id, idsString).asc());
        } else {
            query.orderBy(facility.id.desc());
        }

        query.where(
                fullTextSearch(name, address),
                facilityTypeEq(facilityType),
                gradeEq(grade),
                establishmentYearAfter(withinYears),
                withinRadius(latitude, longitude, radiusKm)
        );

        return query.offset((long) (page - 1) * size)
                .limit(size)
                .fetch();
    }

    private BooleanExpression fullTextSearch(String name, String address) {

        String rawKeyword = StringUtils.hasText(name) ? name : address;
        if (!StringUtils.hasText(rawKeyword)) return null;
        String cleanKeyword = rawKeyword.replace(" ", "");

        return Expressions.numberTemplate(Double.class,
                "function('match', {0}, {1}, {2})",
                facility.name, facility.address, cleanKeyword).gt(0);
    }

    private BooleanExpression facilityTypeEq(String facilityType) {
        if (!StringUtils.hasText(facilityType)) return null;

        return switch (facilityType) {
            case "실버타운", "양로원" -> facility.name.contains(facilityType);
            case "요양원" -> facility.facilityType.in("노인요양시설", "노인요양공동생활가정");
            default -> facility.facilityType.eq(facilityType);
        };
    }

    private BooleanExpression gradeEq(String grade) {
        // 1. 값이 없거나 빈 문자열인 경우 조건을 무시 (null 반환 시 where 절에서 무시됨)
        if (!StringUtils.hasText(grade)) {
            return null;
        }

        return facilityGrade.grade.eq(grade);
    }

    private BooleanExpression withinRadius(Double lat, Double lon, Double radiusKm) {
        if (lat == null || lon == null || radiusKm == null) return null;

        // MySQL 8.0 ST_Distance_Sphere(POINT(lon, lat), POINT(target_lon, target_lat)) <= distance
        return Expressions.booleanTemplate(
                "function('ST_Distance_Sphere', function('POINT', {0}, {1}), function('POINT', {2}, {3})) <= {4}",
                lon, lat, facility.longitude, facility.latitude, radiusKm * 1000); // km to meters
    }

    private BooleanExpression establishmentYearAfter(int withinYears) {
        if (withinYears <= 0) return null;
        int targetYear = Year.now().getValue() - withinYears;
        return facility.establishmentDate.goe(targetYear);
    }

}
