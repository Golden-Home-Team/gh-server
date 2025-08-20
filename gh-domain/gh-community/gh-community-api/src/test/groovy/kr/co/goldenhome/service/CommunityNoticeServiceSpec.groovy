package kr.co.goldenhome.service

import kr.co.goldenhome.dto.CommunityNoticeRequest
import kr.co.goldenhome.dto.CommunityNoticeUpdateRequest
import kr.co.goldenhome.entity.CommunityNotice
import kr.co.goldenhome.repository.CommunityNoticeRepository
import spock.lang.Specification

class CommunityNoticeServiceSpec extends Specification{

    CommunityNoticeService communityNoticeService
    CommunityNoticeRepository communityNoticeRepository = Mock()

    def setup() {
        communityNoticeService = new CommunityNoticeService(communityNoticeRepository)
    }

    def "create - communityNoticeRepository 를 호출한다"() {
        given:
        def givenRequest = new CommunityNoticeRequest("공지입니다.", "공지 내용입니다.")
        def givenFacilityId = 1L
        when:
        communityNoticeService.create(givenRequest, givenFacilityId)

        then:
        1 * communityNoticeRepository.save(*_)
    }

    def "update - communityNoticeRepository 를 호출한다"() {
        given:
        def givenRequest = new CommunityNoticeUpdateRequest("수정된 공지입니다.", "수정된 공지 내용입니다.")
        def givenNoticeId = 1L

        when:
        communityNoticeService.update(givenRequest, givenNoticeId)

        then:
        1 * communityNoticeRepository.findById(*_) >> Optional.of(CommunityNotice.builder().title("").content("").build())
    }

    def "read - communityNoticeRepository 를 호출한다"() {
        given:
        def givenNoticeId = 1L

        when:
        communityNoticeService.read(givenNoticeId)

        then:
        1 * communityNoticeRepository.findById(*_) >> Optional.of(Mock(CommunityNotice))
    }

    def "readLatest - communityNoticeRepository 를 호출한다"() {
        given:
        def givenFacilityId = 1L

        when:
        communityNoticeService.readLatest(givenFacilityId)

        then:
        1 * communityNoticeRepository.findTopByFacilityIdOrderByCreatedAtDesc(*_) >> Optional.of(Mock(CommunityNotice))
    }

    def "readAll - communityNoticeRepository 를 호출한다"() {
        given:
        def givenFacilityId = 1L
        def givenLastId = 1L
        def givenPageSize = 10L

        when:
        communityNoticeService.readAll(givenFacilityId, givenLastId, givenPageSize)

        then:
        1 * communityNoticeRepository.findAllInfiniteScroll(*_) >> List.of(Mock(CommunityNotice))
    }

}
