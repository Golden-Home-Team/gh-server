package kr.co.goldenhome.implement;

import kr.co.goldenhome.dto.TermsAgreementRequest;
import kr.co.goldenhome.dto.TermsRequest;
import kr.co.goldenhome.entity.Terms;
import kr.co.goldenhome.entity.TermsType;
import kr.co.goldenhome.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TermsManager {

    private final TermsRepository termsRepository;

    @Transactional
    public void deactivatePreviousTerms(TermsType termsType) {
        termsRepository.findByTermsTypeAndIsActiveTrue(termsType).ifPresent(Terms::deactivate);
    }

    public void register(TermsRequest request) {
        Terms terms = Terms.create(TermsType.valueOf(request.termsType()), request.version(), request.title(), request.content(), request.isMandatory());
        termsRepository.save(terms);
    }

    public List<Terms> getActiveTerms() {
        return termsRepository.findAllByIsActiveTrue();
    }

    public List<Terms> findAllById(TermsAgreementRequest request) {
        List<Long> termsIds = request.agreements().stream()
                .map(TermsAgreementRequest.TermsItem::termsId)
                .toList();
        return termsRepository.findAllById(termsIds);
    }
}
