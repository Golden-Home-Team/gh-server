package kr.co.goldenhome.service;

import kr.co.goldenhome.dto.TermsAgreementRequest;
import kr.co.goldenhome.dto.TermsRequest;
import kr.co.goldenhome.entity.Terms;
import kr.co.goldenhome.entity.TermsType;
import kr.co.goldenhome.implement.TermsAgreementManager;
import kr.co.goldenhome.implement.TermsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TermsService {

    private final TermsManager termsManager;
    private final TermsAgreementManager termsAgreementManager;

    public void create(TermsRequest request) {
        termsManager.deactivatePreviousTerms(TermsType.valueOf(request.termsType()));
        termsManager.register(request);
    }

    public List<Terms> getActiveTerms() {
        return termsManager.getActiveTerms();
    }

    public void agree(TermsAgreementRequest request, Long userId) {
        List<Terms> termsList = termsManager.findAllById(request);
        termsAgreementManager.create(request, termsList, userId);
    }
}
