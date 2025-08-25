package kr.co.goldenhome.service

import kr.co.goldenhome.dto.CommunityScheduleRequest
import kr.co.goldenhome.dto.CommunityScheduleResponse
import kr.co.goldenhome.dto.CommunityScheduleUpdateRequest
import kr.co.goldenhome.entity.CommunitySchedule
import kr.co.goldenhome.repository.CommunityScheduleRepository
import spock.lang.Specification

import java.time.LocalDate
import java.time.Month

class CommunityScheduleServiceSpec extends Specification {

    CommunityScheduleService communityScheduleService
    CommunityScheduleRepository communityScheduleRepository = Mock()

    def setup() {
        communityScheduleService = new CommunityScheduleService(communityScheduleRepository)
    }

    def "write - communityScheduleRepository 를 호출한다"() {
        given:
        def givenRequest = new CommunityScheduleRequest(LocalDate.now(), "")
        def givenFacilityId = 1L

        when:
        communityScheduleService.write(givenRequest, givenFacilityId)

        then:
        1 * communityScheduleRepository.save(_)
    }

    def "update - communityScheduleRepository 를 호출한다"() {
        given:
        def givenRequest = new CommunityScheduleUpdateRequest("")
        def givenFacilityId = 1L

        when:
        communityScheduleService.update(givenRequest, givenFacilityId)

        then:
        1 * communityScheduleRepository.findById(givenFacilityId) >> Optional.of(Mock(CommunitySchedule))
    }

    def "readByMonth - communityScheduleRepository 를 호출한다"() {
        given:
        def givenFacilityId = 1L
        def givenMonth = Month.APRIL

        when:
        communityScheduleService.readByMonth(givenFacilityId, givenMonth)

        then:
        1 * communityScheduleRepository.getByFacilityIdAndMonth(givenFacilityId, givenMonth.getValue()) >> List.of(CommunitySchedule.builder().id(1L).recordDate(LocalDate.now()).content("").facilityId(1L).build())
    }
}
