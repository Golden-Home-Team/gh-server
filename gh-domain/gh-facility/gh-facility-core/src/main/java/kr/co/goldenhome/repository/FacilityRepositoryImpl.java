package kr.co.goldenhome.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.goldenhome.dto.FacilityCombinedDto;
import kr.co.goldenhome.dto.FacilitySearchResponse;
import kr.co.goldenhome.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    /**
     * profileUrl
     * isLiked
     * avgScore
     * 위 필드는 다른 모듈(다른 데이터베이스 가정)로 분리해둔 상태이므로 기본값으로 초기화 후 서비스 단에서 주입
     */
    @Override
    public List<FacilitySearchResponse> search(String name, String address, String facilityType, String grade, String sort, int withinYears, int page, int size, Double latitude, Double longitude, Double radiusKm, List<Long> priorityIds) {

        String rawKeyword = StringUtils.hasText(name) ? name : address;

        return jpaQueryFactory
                .select(Projections.constructor(FacilitySearchResponse.class,
                        facility.id,
                        facility.institutionSymbol,
                        facility.facilityType,
                        facility.name,
                        facility.address,
                        facility.establishmentDate,
                        facilityGrade.grade.coalesce(""),
                        facility.capacity,
                        facility.currentTotal,
                        Expressions.asString(""),
                        facility.latitude,
                        facility.longitude,
                        Expressions.asBoolean(false),
                        Expressions.asNumber(0.0f)
                ))
                .from(facility)
                .leftJoin(facilityGrade).on(facility.institutionSymbol.eq(facilityGrade.institutionSymbol))
                .where(
                        fullTextSearch(name, address),
                        facilityTypeEq(facilityType),
                        gradeEq(grade),
                        establishmentYearAfter(withinYears),
                        withinRadius(latitude, longitude, radiusKm)
                )
                .orderBy(calculateSortOrder(priorityIds, rawKeyword))
                .offset((long) (page - 1) * size)
                .limit(size)
                .fetch();
    }

    private OrderSpecifier<?>[] calculateSortOrder(List<Long> priorityIds, String rawKeyword) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (StringUtils.hasText(rawKeyword)) {
            NumberTemplate<Double> searchScore = Expressions.numberTemplate(Double.class,
                    "function('match_double', {0}, {1}, {2})",
                    facility.name, facility.address, rawKeyword);
            orders.add(searchScore.desc());
        }

        if (priorityIds != null && !priorityIds.isEmpty()) {
            orders.add(new CaseBuilder().when(facility.id.in(priorityIds)).then(0).otherwise(1).asc());
            Object[] args = new Object[priorityIds.size() + 1];
            args[0] = facility.id;
            for (int i = 0; i < priorityIds.size(); i++) {
                args[i + 1] = priorityIds.get(i);
            }
            String placeholders = IntStream.range(0, priorityIds.size())
                    .mapToObj(i -> "{" + (i + 1) + "}")
                    .collect(Collectors.joining(", "));
            orders.add(Expressions.numberTemplate(Integer.class,
                    "FIELD({0}, " + placeholders + ")", args).asc());
        }

        orders.add(facility.id.desc());

        return orders.toArray(new OrderSpecifier[0]);
    }

    private BooleanExpression fullTextSearch(String name, String address) {

        String rawKeyword = StringUtils.hasText(name) ? name : address;
        if (!StringUtils.hasText(rawKeyword)) return null;

        return Expressions.numberTemplate(Double.class,
                "function('match_double', {0}, {1}, {2})",
                facility.name, facility.address, rawKeyword).gt(0);
    }

    private BooleanExpression facilityTypeEq(String facilityType) {
        if (!StringUtils.hasText(facilityType)) return null;

        return switch (facilityType) {
            case "실버타운", "양로원" ->
                    Expressions.numberTemplate(Double.class,
                            "function('match_single', {0}, {1})",
                            facility.name, facilityType).gt(0);
            case "요양원" -> facility.facilityType.in("노인요양시설", "노인요양공동생활가정");
            default -> facility.facilityType.eq(facilityType);
        };
    }

    private BooleanExpression gradeEq(String grade) {
        if (!StringUtils.hasText(grade)) {
            return null;
        }
        return facilityGrade.grade.eq(grade);
    }

    private BooleanExpression withinRadius(Double lat, Double lon, Double radiusKm) {
        if (lat == null || lon == null || radiusKm == null) return null;
        return Expressions.booleanTemplate(
                "function('ST_Distance_Sphere', function('POINT', {0}, {1}), function('POINT', {2}, {3})) <= {4}",
                lon, lat, facility.longitude, facility.latitude, radiusKm * 1000);
    }

    private BooleanExpression establishmentYearAfter(int withinYears) {
        if (withinYears <= 0) return null;
        int targetYear = Year.now().getValue() - withinYears;
        return facility.establishmentDate.goe(targetYear);
    }

}
