package kr.co.goldenhome.implement

import kr.co.goldenhome.dto.TermsAgreementRequest
import kr.co.goldenhome.entity.Terms
import kr.co.goldenhome.entity.TermsAgreement
import kr.co.goldenhome.repository.TermsAgreementRepository
import spock.lang.Specification

class TermsAgreementManagerSpec extends Specification {

    TermsAgreementManager termsAgreementManager
    TermsAgreementRepository termsAgreementRepository = Mock()
    TermsValidator termsValidator = Mock()

    def setup() {
        termsAgreementManager = new TermsAgreementManager(termsAgreementRepository, termsValidator)
    }

    def "새로운 약관 동의 내역을 생성하여 저장한다"() {
        given: "테스트 데이터 준비"
        Long userId = 1L
        Long termsId = 10L
        boolean isAgreed = true

        def terms = Terms.builder().id(termsId).title("서비스 이용약관").build()
        def termsList = [terms]

        def termsItem = new TermsAgreementRequest.TermsItem(termsId, isAgreed)
        def request = new TermsAgreementRequest([termsItem])

        and: "기존 동의 내역이 없음 (Optional.empty 반환)"
        termsAgreementRepository.findByTermsIdAndUserId(termsId, userId) >> Optional.empty()

        when: "메서드 실행"
        termsAgreementManager.create(request, termsList, userId)

        then: "검증"
        1 * termsValidator.validate(terms, termsItem)

        1 * termsAgreementRepository.saveAll({ List<TermsAgreement> list ->
            list.size() == 1
            list[0].userId == userId
            list[0].termsId == termsId
            list[0].isAgreed == isAgreed
        })
    }

    def "이미 존재하는 약관 동의 내역은 상태를 업데이트한다"() {
        given: "테스트 데이터 준비"
        Long userId = 1L
        Long termsId = 10L
        boolean newAgreedStatus = false

        def terms = Terms.builder().id(termsId).build()
        def termsList = [terms]

        def termsItem = new TermsAgreementRequest.TermsItem(termsId, newAgreedStatus)
        def request = new TermsAgreementRequest([termsItem])

        and: "기존 동의 내역이 존재함 (Mock 객체 리턴)"
        def existingAgreement = TermsAgreement.create(userId, termsId, true)
        termsAgreementRepository.findByTermsIdAndUserId(termsId, userId) >> Optional.of(existingAgreement)

        when: "메서드 실행"
        termsAgreementManager.create(request, termsList, userId)

        then: "검증"
        1 * termsValidator.validate(terms, termsItem)

        existingAgreement.isAgreed == newAgreedStatus

        1 * termsAgreementRepository.saveAll({ List list -> list.isEmpty() })
    }
}
