package kr.co.goldenhome.implement;

import kr.co.goldenhome.dto.TermsAgreementRequest;
import kr.co.goldenhome.entity.Terms;

import kr.co.goldenhome.entity.TermsAgreement;
import kr.co.goldenhome.repository.TermsAgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TermsAgreementManager {

    private final TermsAgreementRepository termsAgreementRepository;
    private final TermsValidator termsValidator;

    @Transactional
    public void create(TermsAgreementRequest request, List<Terms> termsList, Long userId) {
        List<TermsAgreement> termsAgreements = new ArrayList<>();
        Map<Long, Terms> termsMap = termsList.stream().collect(Collectors.toMap(Terms::getId, Function.identity()));
        for (TermsAgreementRequest.TermsItem termsItem : request.agreements()) {
            Terms terms = termsMap.get(termsItem.termsId());
            termsValidator.validate(terms, termsItem);
            termsAgreementRepository.findByTermsIdAndUserId(termsItem.termsId(), userId)
                    .ifPresentOrElse(termsAgreement -> termsAgreement.update(termsItem.isAgreed()),
                            () -> termsAgreements.add(TermsAgreement.create(userId, termsItem.termsId(), termsItem.isAgreed()))
                    );
        }
        termsAgreementRepository.saveAll(termsAgreements);
    }
}
