package kr.co.goldenhome.implement;

import kr.co.goldenhome.dto.TermsAgreementRequest;
import kr.co.goldenhome.entity.Terms;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class TermsValidator {

    public void validate(Terms terms, TermsAgreementRequest.TermsItem termsItem) {
        if (terms == null) throw new CustomException(ErrorCode.TERMS_NOT_FOUND, "TermsValidator.validate");
        if (terms.getIsMandatory() && !termsItem.isAgreed()) throw new CustomException(ErrorCode.TERMS_IS_MANDATORY, "TermsValidator.validate");
    }
}
