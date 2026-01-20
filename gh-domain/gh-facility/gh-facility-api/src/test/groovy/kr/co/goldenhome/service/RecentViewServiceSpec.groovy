package kr.co.goldenhome.service

import kr.co.goldenhome.entity.RecentView
import kr.co.goldenhome.repository.RecentViewRepository
import spock.lang.Specification

import java.time.LocalDateTime

class RecentViewServiceSpec extends Specification {

    RecentViewService recentViewService
    RecentViewRepository recentViewRepository = Mock()

    def setup() {
        recentViewService = new RecentViewService(recentViewRepository)
    }

    def "saveOrUpdate - 엔티티가 존재하면 종료한다"() {
        given:
        def givenUserId = 1L
        def givenFacilityId = 1L

        when:
        recentViewService.saveOrUpdate(givenUserId, givenFacilityId)

        then:
        1 * recentViewRepository.findByUserIdAndFacilityId(*_) >> Optional.of(RecentView.builder().viewedAt(LocalDateTime.now()).build())
        0 * recentViewRepository.save(*_)
    }

    def "saveOrUpdate - 엔티티가 존재하지 않으면 저장한다"() {
        given:
        def givenUserId = 1L
        def givenFacilityId = 1L

        when:
        recentViewService.saveOrUpdate(givenUserId, givenFacilityId)

        then:
        1 * recentViewRepository.findByUserIdAndFacilityId(*_) >> Optional.empty()
        1 * recentViewRepository.save(*_)
    }

}
