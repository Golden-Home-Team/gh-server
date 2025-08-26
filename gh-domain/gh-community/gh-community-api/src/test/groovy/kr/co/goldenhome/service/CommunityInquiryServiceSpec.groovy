package kr.co.goldenhome.service

import kr.co.goldenhome.dto.CommunityInquiryRequest
import kr.co.goldenhome.entity.CommunityInquiry
import kr.co.goldenhome.enums.CommunityInquiryType
import kr.co.goldenhome.repository.CommunityInquiryRepository
import spock.lang.Specification

import java.time.LocalDate

class CommunityInquiryServiceSpec extends Specification {

    CommunityInquiryService communityInquiryService
    CommunityInquiryRepository communityInquiryRepository = Mock()

    def setup() {
        communityInquiryService = new CommunityInquiryService(communityInquiryRepository)
    }

    def "write - communityInquiryRepository 를 호출한다"() {
        given:
        def givenRequest = new CommunityInquiryRequest(LocalDate.now(), "", CommunityInquiryType.DIET.name(), false)
        def givenFacilityId = 1L
        def givenUserId = 2L

        when:
        communityInquiryService.write(givenRequest, givenFacilityId, givenUserId)

        then:
        1 * communityInquiryRepository.save(_)
    }

    def "read - communityInquiryRepository 를 호출한다"() {
        given:
        def givenInquiryId = 1L

        when:
        communityInquiryService.read(givenInquiryId)

        then:
        1 * communityInquiryRepository.findById(givenInquiryId) >> Optional.of(Mock(CommunityInquiry))
    }

    def "readAll - communityInquiryRepository 를 호출한다"() {
        given:
        def givenFacilityId = 1L
        def givenLastId = 2L
        def givenPageSize = 20L

        when:
        communityInquiryService.readAll(givenFacilityId, givenLastId, givenPageSize)

        then:
        1 * communityInquiryRepository.findAllInfiniteScroll(*_) >> List.of(Mock(CommunityInquiry))
    }
}
