package kr.co.goldenhome.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TermsAgreementRequest(
        @NotEmpty
        List<TermsItem> agreements
) {
    public record TermsItem(
            @NotNull Long termsId,
            @NotNull Boolean isAgreed
    ) {}
}
