package kr.co.goldenhome.dto;

import kr.co.goldenhome.entity.Terms;
import kr.co.goldenhome.entity.TermsType;

public record TermsResponse(
        TermsType termsType,
        String version,
        String title,
        String content,
        Boolean isMandatory
) {
    public static TermsResponse from(Terms terms) {
        return new TermsResponse(terms.getTermsType(), terms.getVersion(), terms.getTitle(), terms.getContent(), terms.getIsMandatory());
    }
}
