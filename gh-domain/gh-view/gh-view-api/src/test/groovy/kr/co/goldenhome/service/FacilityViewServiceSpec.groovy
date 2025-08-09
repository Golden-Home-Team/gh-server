package kr.co.goldenhome.service

import kr.co.goldenhome.implement.FacilityViewCountBackUpManager
import kr.co.goldenhome.repository.FacilityViewCountRepository
import kr.co.goldenhome.repository.FacilityViewDistributedLockRepository
import spock.lang.Specification

import java.time.Duration

class FacilityViewServiceSpec extends Specification {

    FacilityViewService facilityViewService
    FacilityViewCountRepository facilityViewCountRepository = Mock()
    FacilityViewCountBackUpManager facilityViewCountBackUpManager = Mock()
    FacilityViewDistributedLockRepository facilityViewDistributedLockRepository = Mock()

    def "setup"() {
        facilityViewService = new FacilityViewService(facilityViewCountRepository, facilityViewCountBackUpManager, facilityViewDistributedLockRepository)
    }

    def "increase 메서드가 락 획득에 성공할 경우 정상적으로 조회수를 증가시킨다"() {
        given:
        Long facilityId = 1L
        Long userId = 100L

        facilityViewDistributedLockRepository.lock(*_) >> true
        facilityViewCountRepository.increase(*_) >> 100L

        when:
        facilityViewService.increase(facilityId, userId)

        then:
        1 * facilityViewCountBackUpManager.backUp(*_)
    }

    def "increase 메서드가 락 획득에 실패할 경우, 조회수를 증가시키지 않는다"() {
        given:
        long facilityId = 2L
        long userId = 200L
        def currentCount = 50L
        def ttl = Duration.ofMillis(10)

        facilityViewDistributedLockRepository.lock(facilityId, userId, ttl) >> false
        facilityViewCountRepository.read(facilityId) >> currentCount

        when:
        def result = facilityViewService.increase(facilityId, userId)

        then:
        result == currentCount
        0 * facilityViewCountRepository.increase(facilityId)
        0 * facilityViewCountBackUpManager.backUp(_, _)
    }

    def "count 메서드가 현재 조회수를 올바르게 반환한다"() {
        given:
        long facilityId = 3L
        def currentCount = 75L

        // `read` 메서드가 현재 조회수(75)를 반환하도록 설정
        facilityViewCountRepository.read(facilityId) >> currentCount

        when:
        def result = facilityViewService.count(facilityId)

        then:
        // `count` 메서드의 결과가 예상한 값과 일치하는지 확인
        result == currentCount
    }

}
