package kr.co.goldenhome.service

import kr.co.goldenhome.dto.DailyMedicationRequest
import kr.co.goldenhome.dto.DailyMedicationUpdateRequest
import kr.co.goldenhome.entity.DailyMedication
import kr.co.goldenhome.repository.DailyMedicationRepository
import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalDate

class DailyMedicationServiceSpec extends Specification {

    DailyMedicationService dailyMedicationService
    DailyMedicationRepository dailyMedicationRepository = Mock()

    def setup() {
        dailyMedicationService = new DailyMedicationService(dailyMedicationRepository)
    }

    def "write - dailyMedicationRepository 를 호출한다"() {
        given:
        def givenRequest = new DailyMedicationRequest(LocalDate.of(2025,10,10), "06:10 복약명", null, "특이사항 없음")
        def givenFacilityId = 1L

        when:
        dailyMedicationService.write(givenRequest, givenFacilityId)

        then:
        1 * dailyMedicationRepository.save(_)
    }

    def "update - dailyMedicationRepository 를 호출한다"() {
        given:
        def givenRequest = new DailyMedicationUpdateRequest("06:10 복약명", null, "특이사항 없음")
        def givenDailyMedicationId = 1L

        when:
        dailyMedicationService.update(givenRequest, givenDailyMedicationId)

        then:
        1 * dailyMedicationRepository.findById(*_) >> {
            Long dailyMedicationId ->
                dailyMedicationId == givenDailyMedicationId
                Optional.of(DailyMedication.builder().morningContent().afternoonContent().nightContent().build())
        }
    }

    def "readByDayOfWeek - dailyMedicationRepository 를 호출한다"() {
        given:
        def givenFacilityId = 1L
        def givenDayOfWeek = DayOfWeek.MONDAY
        def givenResponse = DailyMedication.create(givenFacilityId, LocalDate.now(), null, null,null)

        when:
        dailyMedicationService.readByDayOfWeek(givenFacilityId, givenDayOfWeek)

        then:
        1 * dailyMedicationRepository.getLatestByFacilityIdAndDayOfWeek(*_) >> {
            Long facilityId, int dayOfWeek ->
                facilityId == givenFacilityId
                dayOfWeek == givenDayOfWeek.getValue()
                givenResponse
        }
    }
}
