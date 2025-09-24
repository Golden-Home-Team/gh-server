package kr.co.goldenhome.service;

import kr.co.goldenhome.*;
import kr.co.goldenhome.dto.*;
import kr.co.goldenhome.entity.*;
import kr.co.goldenhome.implement.DailyDietReader;
import kr.co.goldenhome.implement.DailyShotReader;
import kr.co.goldenhome.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityQueryService {

    private final CommunityUserRepository communityUserRepository;
    private final CommunityNoticeRepository communityNoticeRepository;
    private final DailyDietReader dailyDietReader;
    private final DailyDietImageApi dailyDietImageApi;
    private final DailyShotReader dailyShotReader;
    private final DailyShotImageApi dailyShotImageApi;
    private final DailyMedicationRepository dailyMedicationRepository;
    private final DailyRehabilitationRepository dailyRehabilitationRepository;
    private final DailyExerciseRepository dailyExerciseRepository;
    private final CommunityScheduleRepository communityScheduleRepository;
    private final UserApi userApi;
    private final FacilityApi facilityApi;

    public boolean isCommunityUser(Long facilityId, Long userId) {
        return communityUserRepository.findByFacilityIdAndUserId(facilityId, userId).isPresent();
    }

    public CommunityCombinedResponse read(Long facilityId) {
        NoticeInfo noticeInfo = getNoticeInfo(facilityId);
        DailyDietInfo dailyDietInfo = getDailyDietInfo(facilityId);
        DailyShotInfo dailyShotInfo = getDailyShotInfo(facilityId);
        DailyMedicationInfo dailyMedicationInfo = getDailyMedicationInfo(facilityId);
        DailyRehabilitationInfo dailyRehabilitationInfo = getDailyRehabilitationInfo(facilityId);
        List<CommunityScheduleResponse> communityScheduleResponses = communityScheduleRepository.getByFacilityIdAndMonth(facilityId, LocalDate.now().getMonthValue()).stream().map(CommunityScheduleResponse::from).toList();
        CommunityUser manager = communityUserRepository.getManager(facilityId);
        String communityManagerName = userApi.getUserName(manager.getUserId());
        FacilityApiResponse facilityApiResponse = facilityApi.get(facilityId);
        return new CommunityCombinedResponse(
                noticeInfo,
                dailyDietInfo,
                dailyShotInfo,
                dailyMedicationInfo,
                dailyRehabilitationInfo,
                communityScheduleResponses,
                communityManagerName,
                facilityApiResponse.name()
        );
    }

    private NoticeInfo getNoticeInfo(Long facilityId) {
        return communityNoticeRepository.findTopByFacilityIdOrderByCreatedAtDesc(facilityId)
                .map(notice -> new NoticeInfo(
                        notice.getId(),
                        notice.getTitle(),
                        notice.getContent(),
                        notice.getCreatedAt()
                ))
                .orElse(new NoticeInfo(null, null, null, null));
    }

    private DailyDietInfo getDailyDietInfo(Long facilityId) {
        Long dailyDietId = dailyDietReader.getLatest(facilityId);
        DailyDietImageResponse imageResponse = null;

        if (dailyDietId != null) {
            DailyDietImageApiResponse apiResponse = dailyDietImageApi.getLatest(dailyDietId);
            if (apiResponse != null) {
                imageResponse = new DailyDietImageResponse(
                        apiResponse.id(),
                        apiResponse.dailyDietType(),
                        apiResponse.imageUrl(),
                        apiResponse.createdAt()
                );
            }
        }
        return new DailyDietInfo(dailyDietId, imageResponse);
    }

    public DailyShotInfo getDailyShotInfo(Long facilityId) {
        DailyShot dailyShot = dailyShotReader.getLatestByFacilityId(facilityId);

        Long dailyShotId = Optional.ofNullable(dailyShot)
                .map(DailyShot::getId)
                .orElse(null);
        String dailyShotContent = Optional.ofNullable(dailyShot)
                .map(DailyShot::getContent)
                .orElse(null);

        DailyShotImageResponse dailyShotImageResponse = null;
        if (dailyShotId != null) {
            DailyShotImageApiResponse apiResponse = dailyShotImageApi.getLatestByDailyShotId(dailyShotId);
            if (apiResponse != null) {
                dailyShotImageResponse = new DailyShotImageResponse(
                        apiResponse.id(),
                        apiResponse.imageUrl(),
                        apiResponse.createdAt()
                );
            }
        }

        return new DailyShotInfo(
                dailyShotId,
                dailyShotContent,
                dailyShotImageResponse
        );
    }

    public DailyMedicationInfo getDailyMedicationInfo(Long facilityId) {
        DailyMedication dailyMedication = dailyMedicationRepository
                .findTopByFacilityIdOrderByCreatedAtDesc(facilityId)
                .orElse(null);

        return Optional.ofNullable(dailyMedication)
                .map(med -> new DailyMedicationInfo(
                        med.getId(),
                        med.getMorningContent(),
                        med.getAfternoonContent(),
                        med.getNightContent()
                ))
                .orElse(new DailyMedicationInfo(null, null, null, null));
    }

    public DailyRehabilitationInfo getDailyRehabilitationInfo(Long facilityId) {
        DailyRehabilitation dailyRehabilitation = dailyRehabilitationRepository
                .findTopByFacilityIdOrderByCreatedAtDesc(facilityId)
                .orElse(null);

        Long dailyRehabilitationId = Optional.ofNullable(dailyRehabilitation)
                .map(DailyRehabilitation::getId)
                .orElse(null);
        String treatment = Optional.ofNullable(dailyRehabilitation)
                .map(DailyRehabilitation::getTreatment)
                .orElse(null);

        List<DailyExerciseResponse> dailyExerciseResponses = List.of();
        if (dailyRehabilitationId != null) {
            dailyExerciseResponses = dailyExerciseRepository
                    .findAllByDailyRehabilitationId(dailyRehabilitationId)
                    .stream()
                    .map(dailyExercise -> new DailyExerciseResponse(
                            dailyRehabilitationId,
                            dailyExercise.getContent(),
                            dailyExercise.getStartTime(),
                            dailyExercise.getEndTime()
                    ))
                    .toList();
        }

        return new DailyRehabilitationInfo(
                dailyRehabilitationId,
                treatment,
                dailyExerciseResponses
        );
    }

    public List<MyCommunityResponse> myJoinedCommunity(Long userId) {
        return communityUserRepository.findByUserId(userId).stream()
                .map(communityUser -> new MyCommunityResponse(communityUser.getFacilityId())).toList();
    }

}
