package kr.co.goldenhome.service

import kr.co.goldenhome.entity.InvitationCode
import kr.co.goldenhome.enums.InvitationCodeStatus
import kr.co.goldenhome.repository.CommunityUserRepository
import kr.co.goldenhome.repository.InvitationCodeRepository
import spock.lang.Specification

class CommunityEntryServiceSpec extends Specification{

    CommunityEntryService communityEntryService;
    CommunityUserRepository communityUserRepository = Mock()
    InvitationCodeRepository invitationCodeRepository = Mock()

    def setup() {
        communityEntryService = new CommunityEntryService(communityUserRepository, invitationCodeRepository)
    }

    def "generateInvitationCode - invitationCodeRepository 를 호출한다."() {
        given:
        def givenFacilityId = 1L
        def givenUserId = 1L
        when:
        communityEntryService.generateInvitationCode(givenFacilityId, givenUserId)

        then:
        1 * invitationCodeRepository.save(*_)
    }

    def "enter - invitationCodeRepository, communityUserRepository 를 호출한다"() {
        given:
        def givenCode = "1234"
        def givenUserId = 1L
        def givenInvitationCode = Optional.of(InvitationCode.builder().status(InvitationCodeStatus.ACTIVE).usedByUserId(null).build())

        when:
        communityEntryService.enter(givenCode, givenUserId)

        then:
        1 * invitationCodeRepository.findByCodeAndStatus(*_) >> givenInvitationCode
        1 * communityUserRepository.save(*_)

    }




}
