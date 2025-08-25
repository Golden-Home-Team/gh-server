package kr.co.goldenhome.service

import kr.co.goldenhome.dto.DailyExerciseRequest
import kr.co.goldenhome.dto.DailyExerciseUpdateRequest
import kr.co.goldenhome.dto.DailyRehabilitationRequest
import kr.co.goldenhome.dto.DailyRehabilitationUpdateRequest
import kr.co.goldenhome.entity.DailyExercise
import kr.co.goldenhome.entity.DailyRehabilitation
import kr.co.goldenhome.repository.DailyExerciseRepository
import kr.co.goldenhome.repository.DailyRehabilitationRepository
import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class DailyRehabilitationServiceSpec extends Specification {

    DailyRehabilitationService dailyRehabilitationService
    DailyRehabilitationRepository dailyRehabilitationRepository = Mock()
    DailyExerciseRepository dailyExerciseRepository = Mock()

    def setup() {
        dailyRehabilitationService = new DailyRehabilitationService(dailyRehabilitationRepository, dailyExerciseRepository)
    }

    def "write - dailyRehabilitationRepository, dailyExerciseRepository 를 호출한다"() {
        given:
        def givenExerciseRequest = List.of(new DailyExerciseRequest("운동 1회", LocalTime.now(), LocalTime.now().plusHours(1)))
        def givenRequest = new DailyRehabilitationRequest(LocalDate.now(), "특이사항없음", givenExerciseRequest)
        def givenFacilityId = 1L
        def givenDailyRehab = DailyRehabilitation.builder().id(1L).build()

        when:
        dailyRehabilitationService.write(givenRequest, givenFacilityId)

        then:
        1 * dailyRehabilitationRepository.save(_) >> givenDailyRehab
        1 * dailyExerciseRepository.saveAll(_)
    }

    def "update - dailyRehabilitationRepository, dailyExerciseRepository 를 호출한다"() {
        given:
        def givenExerciseRequest = List.of(new DailyExerciseUpdateRequest(1L, "운동 1회", LocalTime.now(), LocalTime.now().plusHours(1)))
        def givenRequest = new DailyRehabilitationUpdateRequest("특이사항없음", givenExerciseRequest)
        def givenDailyRehabilitationId = 1L
        def givenDailyRehab = DailyRehabilitation.builder().id(givenDailyRehabilitationId).build()
        def givenDailyExercise = DailyExercise.builder().build()

        when:
        dailyRehabilitationService.update(givenRequest, givenDailyRehabilitationId)

        then:
        1 * dailyRehabilitationRepository.findById(givenDailyRehabilitationId) >> Optional.of(givenDailyRehab)
        1 * dailyExerciseRepository.findById(_) >> Optional.of(givenDailyExercise)
    }

    def "readByDayOfWeek - dailyRehabilitationRepository, dailyExerciseRepository 를 호출한다"() {
        given:
        def givenFacilityId = 1L
        def givenDayOfWeek = DayOfWeek.MONDAY
        def givenDailyRehab = DailyRehabilitation.builder().id(1L).facilityId(givenFacilityId).recordDate(LocalDate.now()).build()
        def givenDailyExercise = DailyExercise.builder().id(1L).content("").dailyRehabilitationId(givenDailyRehab.id).startTime(LocalTime.now()).endTime(LocalTime.now()).build()
        when:
        dailyRehabilitationService.readByDayOfWeek(givenFacilityId, givenDayOfWeek)

        then:
        1 * dailyRehabilitationRepository.getLatestByFacilityIdAndDayOfWeek(*_) >> givenDailyRehab
        1 * dailyExerciseRepository.findAllByDailyRehabilitationId(givenDailyRehab.id) >> List.of(givenDailyExercise)
    }

}
