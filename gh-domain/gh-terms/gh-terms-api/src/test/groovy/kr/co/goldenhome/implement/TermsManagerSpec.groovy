package kr.co.goldenhome.implement

import kr.co.goldenhome.dto.TermsAgreementRequest
import kr.co.goldenhome.dto.TermsRequest
import kr.co.goldenhome.entity.Terms
import kr.co.goldenhome.entity.TermsType
import kr.co.goldenhome.repository.TermsRepository
import spock.lang.Specification

class TermsManagerSpec extends Specification {

    TermsManager termsManager
    TermsRepository termsRepository = Mock()

    def setup() {
        termsManager = new TermsManager(termsRepository)
    }

    def "deactivatePreviousTerms - termsRepository 를 호출한다"() {
        given:
        def givenTermsType = TermsType.LOCATION_SERVICE_TERMS

        when:
        termsManager.deactivatePreviousTerms(givenTermsType)

        then:
        1*termsRepository.findByTermsTypeAndIsActiveTrue(givenTermsType) >> Optional.of(Terms.builder().isActive(true).build())
    }

    def "register - termsRepository 를 호출한다"() {
        given:
        def givenRequest = new TermsRequest(TermsType.LOCATION_SERVICE_TERMS.name(), "1.0", "약관", "내용", true)

        when:
        termsManager.register(givenRequest)

        then:
        1 * termsRepository.save(_)
    }

    def "getActiveTerms - termsRepository 를 호출한다"() {
        when:
        termsManager.getActiveTerms()

        then:
        1 * termsRepository.findAllByIsActiveTrue()
    }

    def "findAllById - termsRepository 를 호출한다"() {
        given:
        def givenRequest = new TermsAgreementRequest(List.of(new TermsAgreementRequest.TermsItem(1L, true)))

        when:
        termsManager.findAllById(givenRequest)

        then:
        1 * termsRepository.findAllById(_)
    }
}

