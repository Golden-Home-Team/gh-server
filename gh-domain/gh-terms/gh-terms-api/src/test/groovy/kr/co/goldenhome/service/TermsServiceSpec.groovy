package kr.co.goldenhome.service

import kr.co.goldenhome.dto.TermsAgreementRequest
import kr.co.goldenhome.dto.TermsRequest
import kr.co.goldenhome.entity.Terms
import kr.co.goldenhome.entity.TermsType
import kr.co.goldenhome.implement.TermsAgreementManager
import kr.co.goldenhome.implement.TermsManager
import spock.lang.Specification

class TermsServiceSpec extends Specification {

    TermsService termsService
    TermsManager termsManager = Mock()
    TermsAgreementManager termsAgreementManager = Mock()

    def setup() {
        termsService = new TermsService(termsManager, termsAgreementManager)
    }

    def "create - termsManager 를 호출한다"() {
        given:
        def givenRequest = new TermsRequest(TermsType.LOCATION_SERVICE_TERMS.name(), "1.0", "title", "content", false)

        when:
        termsService.create(givenRequest)

        then:
        1 * termsManager.deactivatePreviousTerms(TermsType.valueOf(givenRequest.termsType()))
        1 * termsManager.register(givenRequest)
    }

    def "getActiveTerms - termsManager 를 호출한다"() {
        when:
        termsService.getActiveTerms()

        then:
        1 * termsManager.getActiveTerms()
    }

    def "agree - termsManager, termsAgreementManager 를 호출한다"() {
        given:
        def givenRequest = new TermsAgreementRequest(List.of(new TermsAgreementRequest.TermsItem(1, false)))
        def givenUserId = 1
        def expectedResponse = List.of(Terms.builder().build())

        when:
        termsService.agree(givenRequest, givenUserId)

        then:
        1 * termsManager.findAllById(givenRequest) >> expectedResponse
        1 * termsAgreementManager.create(givenRequest, expectedResponse, givenUserId)


    }
}
