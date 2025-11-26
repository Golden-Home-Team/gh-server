package kr.co.goldenhome.implement

import kr.co.goldenhome.dto.TermsAgreementRequest
import kr.co.goldenhome.entity.Terms
import kr.co.goldenhome.exception.CustomException
import kr.co.goldenhome.exception.ErrorCode
import spock.lang.Specification

class TermsValidatorSpec extends Specification {

    TermsValidator termsValidator = new TermsValidator()

    def "validate - terms 가 null 이면 예외를 던진다"() {
        given:
        def givenTerms = null
        def givenTermsItem = new TermsAgreementRequest.TermsItem(1, true)

        when:
        termsValidator.validate(givenTerms, givenTermsItem)

        then:
        CustomException e = thrown()
        e.getErrorCode() == ErrorCode.TERMS_NOT_FOUND

    }

    def "validate - terms 가 필수일 때 동의를 안하면 예외를 던진다"() {
        given:
        def givenTerms = Terms.builder().id(1).isMandatory(true).build()
        def givenTermsItem = new TermsAgreementRequest.TermsItem(1, false)

        when:
        termsValidator.validate(givenTerms, givenTermsItem)

        then:
        CustomException e = thrown()
        e.getErrorCode() == ErrorCode.TERMS_IS_MANDATORY

    }
}
