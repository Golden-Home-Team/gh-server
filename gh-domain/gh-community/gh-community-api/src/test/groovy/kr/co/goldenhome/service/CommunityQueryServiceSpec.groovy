package kr.co.goldenhome.service

import kr.co.goldenhome.DailyDietImageApi
import kr.co.goldenhome.DailyShotImageApi
import kr.co.goldenhome.UserApi
import kr.co.goldenhome.entity.CommunityUser
import kr.co.goldenhome.entity.DailyRehabilitation
import kr.co.goldenhome.entity.DailyShot
import kr.co.goldenhome.implement.DailyDietReader
import kr.co.goldenhome.implement.DailyShotReader
import kr.co.goldenhome.repository.CommunityNoticeRepository
import kr.co.goldenhome.repository.CommunityScheduleRepository
import kr.co.goldenhome.repository.CommunityUserRepository
import kr.co.goldenhome.repository.DailyExerciseRepository
import kr.co.goldenhome.repository.DailyMedicationRepository
import kr.co.goldenhome.repository.DailyRehabilitationRepository
import spock.lang.Specification

class CommunityQueryServiceSpec extends Specification {
    CommunityQueryService communityQueryService
    CommunityUserRepository communityUserRepository = Mock()
    CommunityNoticeRepository communityNoticeRepository = Mock()
    DailyDietReader dailyDietReader = Mock()
    DailyDietImageApi dailyDietImageApi = Mock()
    DailyShotReader dailyShotReader = Mock()
    DailyShotImageApi dailyShotImageApi = Mock()
    DailyMedicationRepository dailyMedicationRepository = Mock()
    DailyRehabilitationRepository dailyRehabilitationRepository = Mock()
    DailyExerciseRepository dailyExerciseRepository = Mock()
    CommunityScheduleRepository communityScheduleRepository = Mock()
    UserApi userApi = Mock()

    def setup() {
        communityQueryService = new CommunityQueryService(
                communityUserRepository,
                communityNoticeRepository,
                dailyDietReader,
                dailyDietImageApi,
                dailyShotReader,
                dailyShotImageApi,
                dailyMedicationRepository,
                dailyRehabilitationRepository,
                dailyExerciseRepository,
                communityScheduleRepository,
                userApi
        )
    }

    def "isCommunityUser - communityUserRepository 를 호출한다"() {
        given:
        def givenFacilityId = 1L
        def givenUserId = 2L

        when:
        communityQueryService.isCommunityUser(givenFacilityId, givenUserId)

        then:
        1 * communityUserRepository.findByFacilityIdAndUserId(givenFacilityId, givenUserId) >> Optional.of(Mock(CommunityUser))
    }

    def "read"() {
        given:
        def givenFacilityId = 1L
        def givenDailyDietId = 2L
        def givenDailyShot = DailyShot.builder().id(3L).build()
        def givenDailyRehabilitation = DailyRehabilitation.builder().id(4L).build()
        def givenCommunityUser = CommunityUser.builder().id(5L).build()

        when:
        communityQueryService.read(givenFacilityId)

        then:
        1 * communityNoticeRepository.findTopByFacilityIdOrderByCreatedAtDesc(givenFacilityId) >> Optional.empty()
        1 * dailyDietReader.getLatest(givenFacilityId) >> givenDailyDietId
        1 * dailyDietImageApi.getLatest(givenDailyDietId) >> null
        1 * dailyShotReader.getLatestByFacilityId(givenFacilityId) >> givenDailyShot
        1 * dailyShotImageApi.getLatestByDailyShotId(givenDailyShot.id) >> null
        1 * dailyMedicationRepository.findTopByFacilityIdOrderByCreatedAtDesc(givenFacilityId) >> Optional.empty()
        1 * dailyRehabilitationRepository.findTopByFacilityIdOrderByCreatedAtDesc(givenFacilityId) >> Optional.of(givenDailyRehabilitation)
        1 * dailyExerciseRepository.findAllByDailyRehabilitationId(givenDailyRehabilitation.id) >> List.of()
        1 * communityScheduleRepository.getByFacilityIdAndMonth(*_) >> List.of()
        1 * communityUserRepository.getManager() >> givenCommunityUser
        1 * userApi.getUserName(givenCommunityUser.userId)

    }
}
